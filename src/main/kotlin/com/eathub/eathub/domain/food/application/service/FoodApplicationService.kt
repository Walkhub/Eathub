package com.eathub.eathub.domain.food.application.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.OptionApplication
import com.eathub.eathub.domain.food.application.domain.repositories.FoodApplicationRepository
import com.eathub.eathub.domain.food.application.presentation.dto.*
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.option.domain.exportmanager.OptionExportManager
import com.eathub.eathub.domain.restaurant.domain.Restaurant
import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.User
import com.eathub.eathub.domain.user.domain.exportmanager.ApplicationUserExportManager
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FoodApplicationService(
    private val foodApplicationRepository: FoodApplicationRepository,
    private val applicationUserExportManager: ApplicationUserExportManager,
    private val foodExportManager: FoodExportManager,
    private val optionExportManager: OptionExportManager,
    private val socketIOServer: SocketIOServer
) {

    @Transactional
    fun createFoodApplication(request: FoodApplicationRequest) {
        val user = applicationUserExportManager.findByUserIdAndApplicationType(request.userName, request.applicationType)
        val foodApplication = buildFoodApplications(user, request)
        val foodApplications = foodApplicationRepository.saveAll(foodApplication)

        val message = buildFoodApplicationMessages(foodApplications)

        sendApplicationMessageToAllClient(message)
    }

    private fun buildFoodApplications(applicationUser: ApplicationUser, request: FoodApplicationRequest): List<FoodApplication> {
        foodExportManager.findFoodsByIds(request.foods.map { it.foodId })

        return request.foods.map {
            val foodApplication = FoodApplication(
                food = foodExportManager.findFoodById(it.foodId),
                applicationUser = applicationUser,
                count = it.count
            )

            buildOptionApplication(it, foodApplication)
                .map { optionApplication -> foodApplication.addOptionApplication(optionApplication) }

            foodApplication
        }
    }

    private fun buildOptionApplication(
        request: ApplicationRequest,
        foodApplication: FoodApplication
    ): List<OptionApplication> {
        val options = getOptions(request)

        return options.map {
            OptionApplication(
                foodApplication = foodApplication,
                option = it
            )
        }
    }

    private fun getOptions(request: ApplicationRequest) =
        optionExportManager.findOptionsByIds(request.optionIds, request.foodId)

    private fun sendApplicationMessageToAllClient(message: FoodApplicationMessages) =
        socketIOServer.broadcastOperations
            .sendEvent(SocketProperties.FOOD_APPLICATION_KEY, message)

//    private fun sendMoneyStatsToAllClient(message: FoodApplicationMessages) =
//        socketIOServer.broadcastOperations
//            .sendEvent(SocketProperties.FOOD_APPLICATION_KEY, message)

    fun getApplicationList(socketIOClient: SocketIOClient, request: GetFoodApplicationListRequest) {
        val applications = getApplications(request)
        val message = buildFoodApplicationMessages(applications)

        sendApplicationMessageToClient(socketIOClient, message)
    }

    private fun getApplications(request: GetFoodApplicationListRequest) =
        foodApplicationRepository.findAllByApplicationDateBetween(request.applicationType)

    private fun buildFoodApplicationMessages(foodApplications: List<FoodApplication>): FoodApplicationMessages {
        val groupedApplications = foodApplications.groupBy { it.food.restaurant }

        val applications = groupedApplications
            .map { (restaurant, applications) ->
                val foodApplicationMessages = getFoodApplicationMessageList(applications)
                getFoodApplicationRestaurantMessage(restaurant, foodApplicationMessages)
            }

        return getFoodApplicationMessages(applications)

    }

    private fun getFoodApplicationMessageList(foodApplications: List<FoodApplication>) =
        foodApplications.map { buildFoodApplicationMessage(it) }

    private fun buildFoodApplicationMessage(foodApplication: FoodApplication) =
        FoodApplicationMessage(
            cost = foodApplication.food.cost,
            count = foodApplication.count,
            foodId = foodApplication.food.id,
            foodName = foodApplication.food.name,
            options = buildOptionApplicationMessage(foodApplication),
            imageUrl = foodApplication.food.picture
        )

    private fun buildOptionApplicationMessage(foodApplication: FoodApplication) =
        foodApplication.optionApplication.map {
            val option = it.option
            OptionsApplicationMessage(
                optionId = option.id,
                optionName = option.value,
                optionCost = option.cost
            )
        }

    private fun getFoodApplicationRestaurantMessage(
        restaurant: Restaurant,
        foodApplications: List<FoodApplicationMessage>
    ) =
        FoodApplicationRestaurantMessages(
            restaurantName = restaurant.name,
            applications = foodApplications,
            costSum = foodApplications.sumOf { it.cost },
            countSum = foodApplications.sumOf { it.count },
            deliveryFee = restaurant.deliveryFee
        )

    private fun getFoodApplicationMessages(foodApplicationRestaurantMessages: List<FoodApplicationRestaurantMessages>) =
        FoodApplicationMessages(foodApplicationRestaurantMessages)

    private fun sendApplicationMessageToClient(socketIOClient: SocketIOClient, message: FoodApplicationMessages) =
        socketIOClient.sendEvent(SocketProperties.FOOD_APPLICATION_LIST_KEY, message)

    fun getMyFoodApplication(socketIOClient: SocketIOClient, request: MyFoodApplicationRequest) {
        val applications = getMyFoodApplication(request)
        val myFoodApplicationMessages = buildMyApplicationMessages(applications)

        sendMyFoodApplicationMessageToClient(socketIOClient, myFoodApplicationMessages)
    }

    private fun getMyFoodApplication(request: MyFoodApplicationRequest) =
        foodApplicationRepository.findAllByApplicationDateBetweenAndUserName(
            request.applicationType,
            request.userName
        )

    private fun buildMyApplicationMessages(applications: List<FoodApplication>): MyFoodApplicationMessages {
        val myFoodApplicationMessageList = applications.map { buildMyApplicationMessage(it) }
        return MyFoodApplicationMessages(myFoodApplicationMessageList)
    }

    private fun buildMyApplicationMessage(application: FoodApplication) =
        MyFoodApplicationMessage(
            restaurantName = application.food.restaurant.name,
            cost = application.food.cost,
            foodName = application.food.name,
            foodId = application.food.id,
            imageUrl = application.food.picture
        )

    private fun sendMyFoodApplicationMessageToClient(
        socketIOClient: SocketIOClient,
        message: MyFoodApplicationMessages
    ) =
        socketIOClient.sendEvent(SocketProperties.FOOD_APPLICATION_MINE_KEY, message)

}
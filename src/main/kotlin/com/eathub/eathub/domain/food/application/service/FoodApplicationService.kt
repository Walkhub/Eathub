package com.eathub.eathub.domain.food.application.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.OptionApplication
import com.eathub.eathub.domain.food.application.domain.repositories.FoodApplicationRepository
import com.eathub.eathub.domain.food.application.presentation.dto.*
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.food.stats.facade.FoodStatsFacade
import com.eathub.eathub.domain.option.domain.exportmanager.OptionExportManager
import com.eathub.eathub.domain.restaurant.domain.Restaurant
import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import com.eathub.eathub.domain.user.domain.exportmanager.ApplicationUserExportManager
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class FoodApplicationService(
    private val foodApplicationRepository: FoodApplicationRepository,
    private val applicationUserExportManager: ApplicationUserExportManager,
    private val foodExportManager: FoodExportManager,
    private val optionExportManager: OptionExportManager,
    private val foodStatsFacade: FoodStatsFacade,
    private val socketIOServer: SocketIOServer,
    private val userExportManager: UserExportManager
) {

    @Transactional
    fun createFoodApplication(request: FoodApplicationRequest, socketIOClient: SocketIOClient) {
        val userName = userExportManager.getUserNameFromSocketIOClient(socketIOClient)
        val user = applicationUserExportManager.findByUserIdAndApplicationType(
            userName,
            request.applicationType,
            LocalDate.now()
        )
        val foodApplications = saveOrUpdateFoodApplication(request, user)

        val message = buildFoodApplicationMessages(foodApplications)

        sendApplicationMessageToAllClient(message)
        sendStatsMessage(request.applicationType)
    }

    private fun saveOrUpdateFoodApplication(
        request: FoodApplicationRequest,
        user: ApplicationUser
    ): MutableList<FoodApplication> {
        val unsavedFoodApplications: List<FoodApplication> = request.foods.map { foodRequest ->
            buildFoodApplication(user, foodRequest)
        }

        return foodApplicationRepository.saveAll(unsavedFoodApplications)
    }

    private fun buildFoodApplication(
        applicationUser: ApplicationUser,
        request: ApplicationRequest
    ): FoodApplication {
        foodExportManager.findFoodById(request.foodId)

        val foodApplication = FoodApplication(
            food = foodExportManager.findFoodById(request.foodId),
            applicationUser = applicationUser,
            count = request.count
        )

        buildOptionApplication(request, foodApplication)
            .map { optionApplication -> foodApplication.addOptionApplication(optionApplication) }

        return foodApplication
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


    fun getApplicationList(socketIOClient: SocketIOClient, request: GetFoodApplicationListRequest) {
        val applications = getApplications(request)
        val message = buildFoodApplicationMessages(applications)

        sendApplicationMessageToClient(socketIOClient, message)
    }

    private fun getApplications(request: GetFoodApplicationListRequest) =
        foodApplicationRepository.findAllByApplicationType(request.applicationType, LocalDate.now())

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
        foodApplications.groupBy { it.food }
            .map {
                it.value.groupBy { foodApplication ->
                    foodApplication.optionApplication.map { it.option.id }
                }
            }
            .flatMap { it.values.map { buildFoodApplicationMessage(it) } }

    private fun buildFoodApplicationMessage(foodApplications: List<FoodApplication>): FoodApplicationMessage {
        val count = foodApplications.sumOf { it.count }
        val foodApplication = foodApplications.first()

        return FoodApplicationMessage(
            cost = foodApplication.food.cost,
            count = count,
            foodId = foodApplication.food.id,
            foodName = foodApplication.food.name,
            options = buildOptionApplicationMessage(foodApplication),
            imageUrl = foodApplication.food.picture
        )
    }

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
            costSum = foodApplications.sumOf { (it.cost + it.options.sumOf { option -> option.optionCost }) * it.count },
            countSum = foodApplications.sumOf { it.count },
            deliveryFee = restaurant.deliveryFee
        )

    private fun getFoodApplicationMessages(foodApplicationRestaurantMessages: List<FoodApplicationRestaurantMessages>) =
        FoodApplicationMessages(foodApplicationRestaurantMessages)

    private fun sendApplicationMessageToClient(socketIOClient: SocketIOClient, message: FoodApplicationMessages) =
        socketIOClient.sendEvent(SocketProperties.FOOD_APPLICATION_LIST_KEY, message)

    private fun sendStatsMessage(applicationType: ApplicationType) {
        foodStatsFacade.sendUsedAmountMessage(applicationType)
    }

    fun getMyFoodApplication(socketIOClient: SocketIOClient, request: MyFoodApplicationRequest) {
        val name = userExportManager.getUserNameFromSocketIOClient(socketIOClient)
        val applications = getMyFoodApplication(request, name)
        val myFoodApplicationMessages = buildMyApplicationMessages(applications)

        sendMyFoodApplicationMessageToClient(socketIOClient, myFoodApplicationMessages)
    }

    private fun getMyFoodApplication(request: MyFoodApplicationRequest, name: String) =
        foodApplicationRepository.findAllByApplicationTypeAndUserName(
            request.applicationType,
            name,
            LocalDate.now()
        )

    private fun buildMyApplicationMessages(applications: List<FoodApplication>): MyFoodApplicationMessages {
        val myFoodApplicationMessageList = applications.groupBy { it.food }
            .map {
                it.value.groupBy { foodApplication ->
                    foodApplication.optionApplication.map { it.option.id }
                }
            }
            .flatMap { it.values.map { buildMyApplicationMessage(it) } }
        return MyFoodApplicationMessages(myFoodApplicationMessageList)
    }

    private fun buildMyApplicationMessage(applications: List<FoodApplication>): MyFoodApplicationMessage {
        val application = applications.first()
        return MyFoodApplicationMessage(
            restaurantName = application.food.restaurant.name,
            cost = application.food.cost,
            foodName = application.food.name,
            foodId = application.food.id,
            imageUrl = application.food.picture
        )
    }

    private fun sendMyFoodApplicationMessageToClient(
        socketIOClient: SocketIOClient,
        message: MyFoodApplicationMessages
    ) = socketIOClient.sendEvent(SocketProperties.FOOD_APPLICATION_MINE_KEY, message)

}
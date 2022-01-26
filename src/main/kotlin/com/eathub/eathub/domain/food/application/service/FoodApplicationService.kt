package com.eathub.eathub.domain.food.application.service

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.OptionApplication
import com.eathub.eathub.domain.food.application.domain.repositories.FoodApplicationRepository
import com.eathub.eathub.domain.food.application.domain.repositories.OptionApplicationRepository
import com.eathub.eathub.domain.food.application.presentation.dto.*
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.option.domain.exportmanager.OptionExportManager
import com.eathub.eathub.domain.user.domain.User
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FoodApplicationService(
    private val foodApplicationRepository: FoodApplicationRepository,
    private val userExportManager: UserExportManager,
    private val foodExportManager: FoodExportManager,
    private val optionExportManager: OptionExportManager,
    private val optionApplicationRepository: OptionApplicationRepository,
    private val socketIOServer: SocketIOServer
) {

    @Transactional
    fun createFoodApplication(request: FoodApplicationRequest) {
        val user = userExportManager.findUserByName(request.userName)
        val foodApplication = buildFoodApplications(user, request)
        val foodApplications = foodApplicationRepository.saveAll(foodApplication)

        val message = buildFoodApplicationMessages(foodApplications)

        sendApplicationMessageToAllClient(message)
    }

    private fun buildFoodApplications(user: User, request: FoodApplicationRequest): List<FoodApplication> {
        foodExportManager.findFoodsByIds(request.foods.map { it.foodId })

        return request.foods.map {
            val foodApplication = FoodApplication(
                food = foodExportManager.findFoodById(it.foodId),
                user = user,
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
        optionExportManager.findOptionsByIds(request.optionIds)

    private fun buildFoodApplicationMessages(foodApplications: List<FoodApplication>): FoodApplicationMessages {
        val groupedApplications = foodApplications.groupBy { it.food.restaurant.name }

        val applications = groupedApplications
            .map { (restaurantName, applications) ->
                val foodApplicationMessages = getFoodApplicationMessageList(applications)
                getFoodApplicationRestaurantMessage(restaurantName, foodApplicationMessages)
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
            options = buildOptionApplicationMessage(foodApplication)
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
        restaurantName: String,
        foodApplications: List<FoodApplicationMessage>
    ) =
        FoodApplicationRestaurantMessages(
            restaurantName = restaurantName,
            applications = foodApplications,
            costSum = foodApplications.sumOf { it.cost },
            countSum = foodApplications.size.toLong()
        )

    private fun getFoodApplicationMessages(foodApplicationRestaurantMessages: List<FoodApplicationRestaurantMessages>) =
        FoodApplicationMessages(foodApplicationRestaurantMessages)

    private fun sendApplicationMessageToAllClient(message: FoodApplicationMessages) =
        socketIOServer.broadcastOperations
            .sendEvent(SocketProperties.FOOD_APPLICATION_KEY, message)

//    private fun sendMoneyStatsToAllClient(message: FoodApplicationMessages) =
//        socketIOServer.broadcastOperations
//            .sendEvent(SocketProperties.FOOD_APPLICATION_KEY, message)

}
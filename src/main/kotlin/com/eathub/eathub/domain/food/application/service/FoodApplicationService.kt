package com.eathub.eathub.domain.food.application.service

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.OptionApplication
import com.eathub.eathub.domain.food.application.domain.repositories.FoodApplicationRepository
import com.eathub.eathub.domain.food.application.domain.repositories.OptionApplicationRepository
import com.eathub.eathub.domain.food.application.presentation.dto.ApplicationRequest
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationMessage
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationMessages
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationRequest
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

        val message = buildFoodApplicationMessages(user, foodApplications)

        sendApplicationMessageToAllClient(message)
    }

    private fun buildFoodApplications(user: User, request: FoodApplicationRequest): List<FoodApplication> {
        foodExportManager.findFoodsByIds(request.foods.map { it.foodId })

        return request.foods.map {
            val foodApplication = FoodApplication(
                food = foodExportManager.findFoodById(it.foodId),
                user = user,
                applicationType = request.applicationType,
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

    private fun buildFoodApplicationMessages(
        user: User,
        foodApplications: List<FoodApplication>
    ): FoodApplicationMessages {
        val foodApplicationsMessages = foodApplications.map { buildFoodApplicationMessage(it) }

        return FoodApplicationMessages(
            userName = user.name,
            userId = user.id,
            foodApplications = foodApplicationsMessages
        )
    }

    private fun buildFoodApplicationMessage(foodApplication: FoodApplication) =
        FoodApplicationMessage(
            imageUrl = foodApplication.food.picture,
            cost = foodApplication.food.cost,
            count = foodApplication.count,
            type = foodApplication.applicationType,
            foodId = foodApplication.food.id,
            foodName = foodApplication.food.name
        )

    private fun sendApplicationMessageToAllClient(message: FoodApplicationMessages) =
        socketIOServer.broadcastOperations
            .sendEvent(SocketProperties.FOOD_APPLICATION_KEY, message)

}
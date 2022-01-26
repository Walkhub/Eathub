package com.eathub.eathub.domain.food.application.service

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.repositories.FoodApplicationRepository
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationMessage
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationMessages
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationRequest
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.user.domain.User
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.stereotype.Service

@Service
class FoodApplicationService(
    private val foodApplicationRepository: FoodApplicationRepository,
    private val userExportManager: UserExportManager,
    private val foodExportManager: FoodExportManager,
    private val socketIOServer: SocketIOServer
) {

    fun foodApplication(request: FoodApplicationRequest) {
        val user = userExportManager.findUserByName(request.userName)
        val foodApplication = buildFoodApplication(user, request)
        val foodApplications = foodApplicationRepository.saveAll(foodApplication)

        val message = buildFoodApplicationMessages(user, foodApplications)

        sendApplicationMessageToAllClient(message)
    }

    private fun buildFoodApplication(user: User, request: FoodApplicationRequest): List<FoodApplication> {

        foodExportManager.findFoodsByIds(request.foods.map { it.foodId })

        return request.foods.map {
            FoodApplication(
                food = foodExportManager.findFoodById(it.foodId),
                user = user,
                applicationType = request.applicationType,
                count = it.count
            )
        }
    }

    private fun buildFoodApplicationMessages(user: User, foodApplications: List<FoodApplication>): FoodApplicationMessages {
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
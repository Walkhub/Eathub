package com.eathub.eathub.domain.food.application.service

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.repositories.FoodApplicationRepository
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationMessage
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationRequest
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import org.springframework.stereotype.Service

@Service
class FoodApplicationService(
    private val foodApplicationRepository: FoodApplicationRepository,
    private val userExportManager: UserExportManager,
    private val foodExportManager: FoodExportManager,
    private val socketIOServer: SocketIOServer
) {

    companion object {
        const val FOOD_APPLICATION_KEY = "food.application"
    }

    fun foodApplication(request: FoodApplicationRequest) {
        val user = userExportManager.findUserByName(request.userName)
        val food = foodExportManager.findFoodById(request.foodId)

        val foodApplication = FoodApplication(
            food = food,
            user = user,
            applicationType = request.applicationType,
            count = request.count
        )

        foodApplicationRepository.save(foodApplication)

        val message = FoodApplicationMessage(
            foodId = food.id,
            cost = food.cost,
            imageUrl = food.picture,
            foodName = food.name,
            type = foodApplication.applicationType,
            userId = user.id,
            userName = user.name
        )

        socketIOServer.broadcastOperations
            .sendEvent(FOOD_APPLICATION_KEY, message)
    }

}
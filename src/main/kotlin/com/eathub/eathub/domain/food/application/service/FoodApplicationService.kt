package com.eathub.eathub.domain.food.application.service

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.repositories.FoodApplicationRepository
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationMessage
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationRequest
import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.user.domain.User
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

        val foodApplication = buildFoodApplication(food, user, request)

        foodApplicationRepository.save(foodApplication)

        val message = buildFoodApplicationMessage(food, user, foodApplication)

        socketIOServer.broadcastOperations
            .sendEvent(FOOD_APPLICATION_KEY, message)
    }

    private fun buildFoodApplication(food: Food, user: User, request: FoodApplicationRequest): FoodApplication {
        return FoodApplication(
            food = food,
            user = user,
            applicationType = request.applicationType,
            count = request.count
        )
    }

    private fun buildFoodApplicationMessage(food: Food, user: User, foodApplication: FoodApplication): FoodApplicationMessage {
        return FoodApplicationMessage(
            foodId = food.id,
            cost = food.cost,
            imageUrl = food.picture,
            foodName = food.name,
            type = foodApplication.applicationType,
            userId = user.id,
            userName = user.name
        )
    }

}
package com.eathub.eathub.domain.food.application.service

import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.repositories.FoodApplicationRepository
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationRequest
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import org.springframework.stereotype.Service

@Service
class FoodApplicationService(
    private val foodApplicationRepository: FoodApplicationRepository,
    private val userExportManager: UserExportManager,
    private val foodExportManager: FoodExportManager
) {

    fun foodApplication(request: FoodApplicationRequest) {
        val user = userExportManager.findUserByName(request.userName)
        val food = foodExportManager.findFoodById(request.foodId)

        val foodApplication = FoodApplication(
            food = food,
            user = user,
            applicationType = request.applicationType
        )

        foodApplicationRepository.save(foodApplication)
    }

}
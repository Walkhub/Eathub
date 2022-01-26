package com.eathub.eathub.domain.food.application.presentation.dto

import com.eathub.eathub.domain.food.application.domain.enums.ApplicationType

class FoodApplicationRequest(
    val userName: String,
    val food: List<ApplicationFoodRequest>,
    val applicationType: ApplicationType
)

class ApplicationFoodRequest(
    val foodId: Long,
    val count: Int
)
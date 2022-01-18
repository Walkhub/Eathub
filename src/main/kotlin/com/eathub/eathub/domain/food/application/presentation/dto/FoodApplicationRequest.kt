package com.eathub.eathub.domain.food.application.presentation.dto

import com.eathub.eathub.domain.food.application.domain.enums.ApplicationType

class FoodApplicationRequest(
    val userName: String,
    val foodId: Long,
    val applicationType: ApplicationType
)
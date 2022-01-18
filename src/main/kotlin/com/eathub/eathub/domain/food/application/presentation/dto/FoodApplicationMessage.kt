package com.eathub.eathub.domain.food.application.presentation.dto

import com.eathub.eathub.domain.food.application.domain.enums.ApplicationType

class FoodApplicationMessage(
    val userName: String,
    val foodName: String,
    val cost: Long,
    val type: ApplicationType,
    val imageUrl: String,
    val userId: Long,
    val foodId: Long
)
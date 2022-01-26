package com.eathub.eathub.domain.food.application.presentation.dto

import com.eathub.eathub.domain.food.application.domain.enums.ApplicationType

class FoodApplicationMessages(
    val userName: String,
    val userId: Long,
    val foodApplications: List<FoodApplicationMessage>
)

class FoodApplicationMessage(
    val imageUrl: String,
    val cost: Long,
    val type: ApplicationType,
    val count: Int,
    val foodId: Long,
    val foodName: String
)
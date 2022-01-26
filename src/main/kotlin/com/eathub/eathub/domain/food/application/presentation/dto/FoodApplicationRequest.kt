package com.eathub.eathub.domain.food.application.presentation.dto

import com.eathub.eathub.domain.food.application.domain.enums.ApplicationType

class FoodApplicationRequest(
    val userName: String,
    val foods: List<ApplicationRequest>,
    val applicationType: ApplicationType
)

class ApplicationRequest(
    val foodId: Long,
    val count: Int,
    val optionIds: List<Long>
)
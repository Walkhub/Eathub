package com.eathub.eathub.domain.food.application.presentation.dto

import com.eathub.eathub.domain.user.domain.enums.ApplicationType

class FoodApplicationRequest(
    val foods: List<ApplicationRequest>,
    val applicationType: ApplicationType
)

class ApplicationRequest(
    val foodId: Long,
    val count: Int,
    val optionIds: List<Long>
)
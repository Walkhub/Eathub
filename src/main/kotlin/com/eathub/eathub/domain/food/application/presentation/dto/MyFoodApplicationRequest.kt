package com.eathub.eathub.domain.food.application.presentation.dto

import com.eathub.eathub.domain.user.domain.enums.ApplicationType

class MyFoodApplicationRequest(
    val userName: String,
    val applicationType: ApplicationType
)
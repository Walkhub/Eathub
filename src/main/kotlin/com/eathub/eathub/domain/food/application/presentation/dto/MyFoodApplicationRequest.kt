package com.eathub.eathub.domain.food.application.presentation.dto

import java.time.LocalDateTime

class MyFoodApplicationRequest(
    val userName: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)
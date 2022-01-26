package com.eathub.eathub.domain.food.application.presentation.dto

import java.time.LocalDateTime

class GetFoodApplicationListRequest(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)
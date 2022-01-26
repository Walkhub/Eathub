package com.eathub.eathub.domain.food.application.presentation.dto

class FoodStatsMessage(
    val usedAmount: Long,
    val amountPerPerson: Int,
    val remainedAmount: Long
)
package com.eathub.eathub.domain.food.stats.presentation.dto

class FoodStatsMessage(
    val usedAmount: Long,
    val amountPerPerson: Long,
    val remainedAmount: Long,
    val totalUsedAmount: Long
)
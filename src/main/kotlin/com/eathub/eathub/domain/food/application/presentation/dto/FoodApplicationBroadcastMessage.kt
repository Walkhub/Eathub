package com.eathub.eathub.domain.food.application.presentation.dto

class FoodApplicationBroadcastMessage(
    val totalAmount: Long,
    val usedAmount: Long,
    val remainAmount: Long
)
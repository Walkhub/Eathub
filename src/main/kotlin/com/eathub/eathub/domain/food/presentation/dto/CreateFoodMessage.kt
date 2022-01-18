package com.eathub.eathub.domain.food.presentation.dto

class CreateFoodMessage(
    val restaurantName: String,
    val restaurantId: Long,
    val name: String,
    val imageUrl: String,
    val cost: Long,
    val foodId: Long
)
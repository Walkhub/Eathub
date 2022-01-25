package com.eathub.eathub.domain.food.presentation.dto

class CreateFoodMessage(
    val restaurantName: String,
    val restaurantId: Long,
    val foodName: String,
    val foodPicture: String,
    val foodCost: Long,
    val foodId: Long
)
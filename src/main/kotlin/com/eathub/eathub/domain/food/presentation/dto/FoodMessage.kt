package com.eathub.eathub.domain.food.presentation.dto

class FoodMessage(
    val foodName: String,
    val foodPicture: String,
    val foodCost: Long,
    val foodScore: Double?,
    val restaurantName: String
)

class FoodMessages(
    val foods: List<FoodMessage>
)
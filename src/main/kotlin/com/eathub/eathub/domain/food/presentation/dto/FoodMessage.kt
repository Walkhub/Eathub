package com.eathub.eathub.domain.food.presentation.dto

class FoodMessage(
    val foodName: String,
    val foodPicture: String,
    val foodCost: Long,
    val restaurantName: String,
    val foodId: Long,
    val foodScore: Double
)

class FoodMessages(
    val foods: List<FoodMessage>
)
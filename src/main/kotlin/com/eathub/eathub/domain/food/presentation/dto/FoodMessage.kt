package com.eathub.eathub.domain.food.presentation.dto

class FoodResponse(
    val foodName: String,
    val foodPicture: String,
    val foodCost: Long,
    val foodScore: Double?,
    val restaurantName: String
)

class FoodListResponse(
    val foods: List<FoodResponse>
)
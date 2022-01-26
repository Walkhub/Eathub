package com.eathub.eathub.domain.food.application.presentation.dto

class FoodApplicationMessages(
    val foodApplications: List<FoodApplicationRestaurantMessages>
)

class FoodApplicationRestaurantMessages(
    val countSum: Int,
    val costSum: Long,
    val restaurantName: String,
    val applications: List<FoodApplicationMessage>
)

class FoodApplicationMessage(
    val cost: Long,
    val count: Int,
    val foodId: Long,
    val foodName: String,
    val imageUrl: String,
    val options: List<OptionsApplicationMessage>
)

class OptionsApplicationMessage(
    val optionId: Long,
    val optionName: String,
    val optionCost: Long
)
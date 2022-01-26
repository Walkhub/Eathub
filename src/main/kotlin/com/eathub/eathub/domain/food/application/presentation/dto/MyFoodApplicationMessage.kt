package com.eathub.eathub.domain.food.application.presentation.dto

class MyFoodApplicationMessages(
    val applications: List<MyFoodApplicationMessage>
)

class MyFoodApplicationMessage(
    val foodId: Long,
    val foodName: String,
    val imageUrl: String,
    val restaurantName: String,
    val cost: Long
)
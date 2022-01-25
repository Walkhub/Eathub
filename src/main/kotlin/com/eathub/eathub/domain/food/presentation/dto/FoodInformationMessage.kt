package com.eathub.eathub.domain.food.presentation.dto

class FoodInformationMessage(
    val name: String,
    val cost: Long,
    val restaurantName: String,
    val restaurantId: Long,
    val imageUrl: String
)
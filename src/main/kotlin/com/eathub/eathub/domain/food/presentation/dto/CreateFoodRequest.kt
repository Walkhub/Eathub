package com.eathub.eathub.domain.food.presentation.dto

class CreateFoodRequest(
    val restaurantId: Long,
    val name: String,
    val imageUrl: String,
    val cost: Long
)
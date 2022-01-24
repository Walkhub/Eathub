package com.eathub.eathub.domain.food.domain.repositories.dto

class FoodInformationResponse(
    val name: String,
    val cost: Long,
    val totalCount: Int,
    val rank: Int,
    val restaurantName: String,
    val restaurantId: Long,
    val reviewAverage: Double
)
package com.eathub.eathub.domain.food.application.presentation.dto

class FoodApplicationRequest(
    val userName: String,
    val foods: List<ApplicationRequest>
)

class ApplicationRequest(
    val foodId: Long,
    val count: Int,
    val optionIds: List<Long>
)
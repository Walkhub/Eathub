package com.eathub.eathub.domain.food.exceptions

import com.eathub.eathub.global.exception.property.ExceptionProperty

enum class FoodExceptionErrorCode(
    override val status: Int,
    override val koreanMessage: String,
    override val errorMessage: String
) : ExceptionProperty {
    FOOD_NOT_FOUND(404, "음식을 찾을 수 없습니다.", "Can't find food")
}
package com.eathub.eathub.domain.restaurant.exceptions

import com.eathub.eathub.global.exception.property.ExceptionProperty

enum class RestaurantExceptionErrorCode(
    override val status: Int,
    override val koreanMessage: String,
    override val errorMessage: String
) : ExceptionProperty {
    RESTAURANT_NOT_FOUND(404, "식당을 찾을 수 없습니다.", "Can't find restaurant")
}
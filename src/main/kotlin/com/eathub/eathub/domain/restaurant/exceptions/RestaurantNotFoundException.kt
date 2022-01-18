package com.eathub.eathub.domain.restaurant.exceptions

import com.eathub.eathub.global.exception.GlobalException

class RestaurantNotFoundException private constructor(): GlobalException(RestaurantExceptionErrorCode.RESTAURANT_NOT_FOUND) {
    companion object {
        @JvmField
        val EXCEPTION = RestaurantNotFoundException()
    }
}
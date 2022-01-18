package com.eathub.eathub.domain.food.exceptions

import com.eathub.eathub.global.exception.GlobalException

class FoodNotFoundException private constructor(): GlobalException(FoodExceptionErrorCode.FOOD_NOT_FOUND) {
    companion object {
        @JvmField
        val EXCEPTION = FoodNotFoundException()
    }
}
package com.eathub.eathub.global.exception.exceptions

import com.eathub.eathub.global.exception.GlobalException
import com.eathub.eathub.global.exception.property.GlobalExceptionErrorCode

class InternalServerError private constructor() : GlobalException(GlobalExceptionErrorCode.UNEXPECTED) {
    companion object {
        @JvmField
        val EXCEPTION = InternalServerError()
    }
}
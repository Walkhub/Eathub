package com.eathub.eathub.global.exception

import com.eathub.eathub.global.exception.property.ExceptionProperty

open class GlobalException(private val property: ExceptionProperty): RuntimeException() {
    val status: Int
    get() = property.status

    val errorMessage
    get() = property.errorMessage

    val koreanMessage
    get() = property.koreanMessage
}
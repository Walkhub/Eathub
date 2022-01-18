package com.eathub.eathub.global.exception.property

enum class GlobalExceptionErrorCode(
    override val errorMessage: String,
    override val status: Int,
    override val koreanMessage: String
) : ExceptionProperty {
    UNEXPECTED("Unexpected Exception Occurred", 500, "예기치 않은 오류가 발생했습니다."),
}

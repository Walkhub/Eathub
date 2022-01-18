package com.eathub.eathub.domain.user.exceptions

import com.eathub.eathub.global.exception.property.ExceptionProperty

enum class UserExceptionErrorCode(
    override val status: Int,
    override val koreanMessage: String,
    override val errorMessage: String
) : ExceptionProperty {
    USER_NAME_NOT_FOUND(404, "이름에 해당하는 사용자를 찾을 수 없습니다.", "Can't find userName"),
    USER_ID_NOT_FOUND(404, "아이디에 해당하는 사용자를 찾을 수 없습니다.", "Can't find userId")
}
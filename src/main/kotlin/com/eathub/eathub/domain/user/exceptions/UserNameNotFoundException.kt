package com.eathub.eathub.domain.user.exceptions

import com.eathub.eathub.global.exception.GlobalException

class UserNameNotFoundException private constructor() : GlobalException(UserExceptionErrorCode.USER_NAME_NOT_FOUND) {
    companion object {
        @JvmField
        val EXCEPTION = UserNameNotFoundException()
    }
}
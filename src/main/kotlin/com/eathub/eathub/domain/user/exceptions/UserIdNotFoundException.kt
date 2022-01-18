package com.eathub.eathub.domain.user.exceptions

import com.eathub.eathub.global.exception.GlobalException

class UserIdNotFoundException private constructor() : GlobalException(UserExceptionErrorCode.USER_ID_NOT_FOUND) {
    companion object {
        @JvmField
        val EXCEPTION = UserIdNotFoundException()
    }
}
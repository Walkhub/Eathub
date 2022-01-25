package com.eathub.eathub.domain.review.exceptions

import com.eathub.eathub.global.exception.GlobalException

class ReviewAlreadyWroteException : GlobalException(ReviewErrorCode.ALREADY_WRITE) {
    companion object {
        @JvmField
        val EXCEPTION = ReviewAlreadyWroteException()
    }
}
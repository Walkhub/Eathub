package com.eathub.eathub.domain.review.exceptions

import com.eathub.eathub.global.exception.GlobalException

class ReviewIdNotFoundException : GlobalException(ReviewErrorCode.ID_NOT_FOUND) {
    companion object {
        @JvmField
        val EXCEPTION = ReviewIdNotFoundException()
    }
}
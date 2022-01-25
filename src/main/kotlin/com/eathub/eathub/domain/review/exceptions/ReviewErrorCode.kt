package com.eathub.eathub.domain.review.exceptions

import com.eathub.eathub.global.exception.property.ExceptionProperty

enum class ReviewErrorCode(
    override val status: Int,
    override val koreanMessage: String,
    override val errorMessage: String
) : ExceptionProperty {
    ALREADY_WRITE(409, "이미 리뷰를 작성하셨습니다", "Review Already Wrote Exception")
}
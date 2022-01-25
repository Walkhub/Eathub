package com.eathub.eathub.domain.review.exceptions

import com.eathub.eathub.global.exception.property.ExceptionProperty

enum class ReviewErrorCode(
    override val status: Int,
    override val koreanMessage: String,
    override val errorMessage: String
) : ExceptionProperty {
    ALREADY_WRITE(409, "이미 리뷰를 작성하셨습니다", "Review Already Wrote Exception"),
    ID_NOT_FOUND(404, "id에 해당하는 리뷰를 찾을 수 없습니다.", "Cannot Find Review By Id")
}
package com.eathub.eathub.domain.review.presentation.dto

import java.time.LocalDateTime

class CreateReviewMessage(
    val content: String,
    val score: Double,
    val foodId: Long,
    val userName: String,
    val userId: Long,
    val createAt: LocalDateTime
)
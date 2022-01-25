package com.eathub.eathub.domain.review.presentation.dto

import java.time.LocalDateTime

class GetReviewMessageList(
    val totalAmount: Int,
    val rank: Int,
    val reviewAverage: Double,
    val reviewMessages: List<GetReviewMessage>
)

class GetReviewMessage(
    val content: String,
    val score: Double,
    val foodId: Long,
    val userName: String,
    val userId: Long,
    val createAt: LocalDateTime
)
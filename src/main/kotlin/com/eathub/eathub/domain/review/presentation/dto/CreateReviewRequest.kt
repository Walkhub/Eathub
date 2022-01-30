package com.eathub.eathub.domain.review.presentation.dto

class CreateReviewRequest(
    val content: String,
    val score: Double,
    val foodId: Long
)
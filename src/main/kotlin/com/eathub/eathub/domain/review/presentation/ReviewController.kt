package com.eathub.eathub.domain.review.presentation

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.annotation.OnEvent
import com.eathub.eathub.domain.review.presentation.dto.CreateReviewRequest
import com.eathub.eathub.domain.review.presentation.dto.GetReviewListRequest
import com.eathub.eathub.domain.review.service.ReviewService
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val reviewService: ReviewService
) {
    @OnEvent("/review/create")
    fun createReview(request: CreateReviewRequest) {
        reviewService.createReview(request)
    }

    @OnEvent("/review/list")
    fun getReviews(socketIOClient: SocketIOClient, request: GetReviewListRequest) {
        reviewService.getReviews(socketIOClient, request)
    }
}
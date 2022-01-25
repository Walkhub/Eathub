package com.eathub.eathub.domain.review.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.review.domain.Review
import com.eathub.eathub.domain.review.domain.repositories.ReviewRepository
import com.eathub.eathub.domain.review.exceptions.ReviewAlreadyWroteException
import com.eathub.eathub.domain.review.presentation.dto.CreateReviewMessage
import com.eathub.eathub.domain.review.presentation.dto.CreateReviewRequest
import com.eathub.eathub.domain.review.presentation.dto.GetReviewListRequest
import com.eathub.eathub.domain.review.presentation.dto.GetReviewMessage
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import org.springframework.stereotype.Service
import java.sql.SQLIntegrityConstraintViolationException

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val userExportManager: UserExportManager,
    private val foodExportManager: FoodExportManager,
    private val socketIOServer: SocketIOServer
) {

    companion object {
        const val CREATE_REVIEW_KEY = "review.create"
        const val GET_REVIEW_KEY = "review.list"
    }

    fun createReview(request: CreateReviewRequest) {
        val review = buildReview(request)
        val savedReview = reviewRepository.save(review)

        val message = buildCreateReviewMessage(savedReview)
        sendCreateReviewMessageToAllClient(message)
    }

    private fun buildReview(request: CreateReviewRequest): Review {
        val user = userExportManager.findUserByName(request.userName)
        val food = foodExportManager.findFoodById(request.foodId)

        return Review(
            user = user,
            food = food,
            score = request.score,
            content = request.content
        )
    }

    private fun saveReview(review: Review) = try {
        reviewRepository.save(review)
    } catch (e: SQLIntegrityConstraintViolationException) {
        throw ReviewAlreadyWroteException.EXCEPTION
    }

    private fun buildCreateReviewMessage(review: Review) =
        CreateReviewMessage(
            score = review.score,
            content = review.content,
            createAt = review.createAt!!,
            userName = review.user.name,
            userId = review.user.id,
            foodId = review.food.id
        )

    private fun sendCreateReviewMessageToAllClient(message: CreateReviewMessage) =
        socketIOServer.broadcastOperations.sendEvent(CREATE_REVIEW_KEY, message)

    fun getReviews(socketIOClient: SocketIOClient, request: GetReviewListRequest) {
        val food = foodExportManager.findFoodById(request.foodId)
        val reviews = reviewRepository.findAllReviewsByFoodId(food)

        val message = buildReviewMessageList(reviews)
        sendReviewMessage(socketIOClient, message)
    }

    private fun buildReviewMessageList(reviews: List<Review>) =
        reviews.map { buildReviewMessage(it) }


    private fun buildReviewMessage(review: Review) =
        GetReviewMessage(
            userId = review.user.id,
            foodId = review.food.id,
            userName = review.user.name,
            createAt = review.createAt!!,
            content = review.content,
            score = review.score
        )

    private fun sendReviewMessage(socketIOClient: SocketIOClient, message: List<GetReviewMessage>) =
        socketIOClient.sendEvent(GET_REVIEW_KEY, message)
}
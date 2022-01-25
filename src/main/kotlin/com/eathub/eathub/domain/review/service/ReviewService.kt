package com.eathub.eathub.domain.review.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.review.domain.Review
import com.eathub.eathub.domain.review.domain.ReviewInformation
import com.eathub.eathub.domain.review.domain.repositories.FoodInformationRepository
import com.eathub.eathub.domain.review.domain.repositories.ReviewRepository
import com.eathub.eathub.domain.review.exceptions.ReviewAlreadyWroteException
import com.eathub.eathub.domain.review.exceptions.ReviewIdNotFoundException
import com.eathub.eathub.domain.review.presentation.dto.CreateReviewMessage
import com.eathub.eathub.domain.review.presentation.dto.CreateReviewRequest
import com.eathub.eathub.domain.review.presentation.dto.GetReviewListRequest
import com.eathub.eathub.domain.review.presentation.dto.GetReviewMessage
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.SQLIntegrityConstraintViolationException

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val reviewInformationRepository: FoodInformationRepository,
    private val userExportManager: UserExportManager,
    private val foodExportManager: FoodExportManager,
    private val socketIOServer: SocketIOServer
) {

    companion object {
        const val CREATE_REVIEW_KEY = "review.create"
        const val GET_REVIEW_KEY = "review.list"
    }

    @Transactional
    fun createReview(request: CreateReviewRequest) {
        val review = buildReview(request)
        val savedReview = saveReview(review)

        val reviewInformation = findReviewInformationById(savedReview.id)
        val message = buildCreateReviewMessage(reviewInformation)
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

    private fun findReviewInformationById(id: Long) =
        reviewInformationRepository.findByIdOrNull(id) ?: throw ReviewIdNotFoundException.EXCEPTION

    private fun buildCreateReviewMessage(review: ReviewInformation) =
        CreateReviewMessage(
            score = review.score,
            content = review.content,
            createAt = review.createAt,
            userName = review.userName,
            userId = review.userId,
            foodId = review.foodId,
            rank = review.scoreRank,
            totalAmount = review.totalAmount,
            reviewAverage = review.reviewAverage
        )

    private fun sendCreateReviewMessageToAllClient(message: CreateReviewMessage) =
        socketIOServer.broadcastOperations.sendEvent(CREATE_REVIEW_KEY, message)

    fun getReviews(socketIOClient: SocketIOClient, request: GetReviewListRequest) {
        val reviewInformations = findReviewInformationsByFoodId(request.foodId)
        val message = buildReviewMessageList(reviewInformations)

        sendReviewMessage(socketIOClient, message)
    }

    private fun findReviewInformationsByFoodId(foodId: Long) =
        reviewInformationRepository.findAllByFoodId(foodId)

    private fun buildReviewMessageList(reviewInformations: List<ReviewInformation>) =
        reviewInformations.map { buildReviewMessage(it) }


    private fun buildReviewMessage(review: ReviewInformation) =
        GetReviewMessage(
            score = review.score,
            content = review.content,
            createAt = review.createAt,
            userName = review.userName,
            userId = review.userId,
            foodId = review.foodId,
            rank = review.scoreRank,
            totalAmount = review.totalAmount,
            reviewAverage = review.reviewAverage
        )

    private fun sendReviewMessage(socketIOClient: SocketIOClient, message: List<GetReviewMessage>) =
        socketIOClient.sendEvent(GET_REVIEW_KEY, message)
}
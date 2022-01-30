package com.eathub.eathub.domain.review.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.exceptions.FoodNotFoundException
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.review.domain.Review
import com.eathub.eathub.domain.review.domain.ReviewInformation
import com.eathub.eathub.domain.review.domain.repositories.ReviewInformationRepository
import com.eathub.eathub.domain.review.domain.repositories.ReviewRepository
import com.eathub.eathub.domain.review.exceptions.ReviewAlreadyWroteException
import com.eathub.eathub.domain.review.presentation.dto.*
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.SQLIntegrityConstraintViolationException

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val reviewInformationRepository: ReviewInformationRepository,
    private val userExportManager: UserExportManager,
    private val foodExportManager: FoodExportManager,
    private val socketIOServer: SocketIOServer
) {

    @Transactional
    fun createReview(socketIOClient: SocketIOClient, request: CreateReviewRequest) {
        val name = userExportManager.getUserNameFromSocketIOClient(socketIOClient)
        val review = buildReview(request, name)
        saveReview(review)

        val reviewInformation = findReviewInformationByFoodId(request.foodId)
        val message = buildCreateReviewMessage(reviewInformation, review)
        sendCreateReviewMessageToAllClient(message)
    }

    private fun buildReview(request: CreateReviewRequest, name: String): Review {
        val user = userExportManager.findUserByName(name)
        val food = foodExportManager.findFoodById(request.foodId)

        return Review(
            user = user,
            food = food,
            score = request.score,
            content = request.content
        )
    }

    private fun saveReview(review: Review) = try {
        reviewRepository.saveAndFlush(review)
    } catch (e: SQLIntegrityConstraintViolationException) {
        throw ReviewAlreadyWroteException.EXCEPTION
    }

    private fun buildCreateReviewMessage(reviewInformation: ReviewInformation, review: Review) =
        CreateReviewMessage(
            score = review.score,
            content = review.content,
            createAt = review.createAt,
            userName = review.user.name,
            userId = review.user.id,
            foodId = review.food.id,
            rank = reviewInformation.scoreRank,
            totalAmount = reviewInformation.totalAmount,
            reviewAverage = reviewInformation.reviewAverage
        )

    private fun sendCreateReviewMessageToAllClient(message: CreateReviewMessage) =
        socketIOServer.getRoomOperations(SocketProperties.getFoodRoomName(message.foodId))
            .sendEvent(SocketProperties.CREATE_REVIEW_KEY, message)

    fun getReviews(socketIOClient: SocketIOClient, request: GetReviewListRequest) {
        val reviews = findReviewsByFoodId(request.foodId)
        val reviewInformations = findReviewInformationByFoodId(request.foodId)

        val message = buildReviewMessageList(reviewInformations, reviews)

        sendReviewMessage(socketIOClient, message)
    }

    private fun findReviewsByFoodId(foodId: Long) =
        reviewRepository.findAllReviewsByFoodId(foodId)

    private fun buildReviewMessageList(
        reviewInformations: ReviewInformation,
        reviews: List<Review>
    ): GetReviewMessageList {
        val reviewMessages = reviews.map { buildReviewMessage(it) }
        return GetReviewMessageList(
            rank = reviewInformations.scoreRank,
            totalAmount = reviewInformations.totalAmount,
            reviewAverage = reviewInformations.reviewAverage,
            reviewMessages = reviewMessages
        )
    }

    private fun buildReviewMessage(review: Review) =
        GetReviewMessage(
            score = review.score,
            content = review.content,
            createAt = review.createAt,
            userName = review.user.name,
            userId = review.user.id,
            foodId = review.food.id
        )

    private fun findReviewInformationByFoodId(foodId: Long) =
        reviewInformationRepository.findByIdOrNull(foodId) ?: throw FoodNotFoundException.EXCEPTION

    private fun sendReviewMessage(socketIOClient: SocketIOClient, message: GetReviewMessageList) =
        socketIOClient.sendEvent(SocketProperties.GET_REVIEW_KEY, message)
}
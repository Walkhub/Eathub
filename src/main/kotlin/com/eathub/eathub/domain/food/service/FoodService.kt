package com.eathub.eathub.domain.food.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.food.domain.FoodInformation
import com.eathub.eathub.domain.food.domain.repositories.FoodInformationRepository
import com.eathub.eathub.domain.food.domain.repositories.FoodRepository
import com.eathub.eathub.domain.food.exceptions.FoodNotFoundException
import com.eathub.eathub.domain.food.presentation.dto.*
import com.eathub.eathub.domain.restaurant.domain.Restaurant
import com.eathub.eathub.domain.restaurant.domain.exportmanager.RestaurantExportManager
import com.eathub.eathub.domain.review.domain.Review
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FoodService(
    private val socketIOServer: SocketIOServer,
    private val foodRepository: FoodRepository,
    private val foodInformationRepository: FoodInformationRepository,
    private val restaurantExportManager: RestaurantExportManager
) {
    companion object {
        private const val CREATE_FOOD_KEY = "food.create"
        private const val FOOD_LIST_KEY = "food.list"
        private const val FOOD_INFO_KEY = "food.information"
    }

    fun createNewFood(request: CreateFoodRequest) {
        val restaurant = restaurantExportManager.findRestaurantById(request.restaurantId)
        val food = buildFoodFromRequestAndRestaurant(request, restaurant)
        val savedFood = foodRepository.save(food)
        val message = buildFoodMessageFromRestaurantAndSavedFood(restaurant, savedFood)

        sendCreateFoodMessageToAllClient(message)
    }

    private fun buildFoodFromRequestAndRestaurant(request: CreateFoodRequest, restaurant: Restaurant) =
        Food(
            name = request.name,
            cost = request.cost,
            picture = request.imageUrl,
            restaurant = restaurant
        )

    private fun buildFoodMessageFromRestaurantAndSavedFood(restaurant: Restaurant, savedFood: Food) =
        CreateFoodMessage(
            restaurantId = restaurant.id,
            restaurantName = restaurant.name,
            name = savedFood.name,
            cost = savedFood.cost,
            foodId = savedFood.id,
            imageUrl = savedFood.picture
        )

    private fun sendCreateFoodMessageToAllClient(createFoodMessage: CreateFoodMessage) =
        socketIOServer.broadcastOperations
            .sendEvent(CREATE_FOOD_KEY, createFoodMessage)

    fun getFoodList(socketIOClient: SocketIOClient) {
        val foods = foodRepository.findAllBy()
        val foodResponse = buildFoodMessages(foods)

        sendFoodListToClient(foodResponse, socketIOClient)
    }

    private fun buildFoodMessages(foods: List<Food>): FoodMessages {
        val foodResponses = foods.map { buildFoodMessage(it) }
        return FoodMessages(foodResponses)
    }

    private fun buildFoodMessage(food: Food) =
        FoodMessage(
            foodName = food.name,
            foodCost = food.cost,
            foodPicture = food.picture,
            foodScore = getRateAverage(food.review),
            restaurantName = food.restaurant.name,
            foodId = food.id
        )

    private fun sendFoodListToClient(foodMessages: FoodMessages, socketIOClient: SocketIOClient) =
        socketIOClient.sendEvent(FOOD_LIST_KEY, foodMessages)

    private fun getRateAverage(reviews: List<Review>): Double? {
        val isReviewExists = reviews.isNotEmpty()
        val doubleAverage = reviews.map { it.score }
            .average()

        val roundedAverage = String.format("%.2f", doubleAverage).toDouble()

        return when (isReviewExists) {
            true -> roundedAverage
            false -> null
        }
    }

    fun getFoodInformation(request: FoodInformationRequest, socketIOClient: SocketIOClient) {
        val food = getFoodEntity(request.foodId)
        val message = buildFoodInformationMessage(food)

        sendFoodInformationToClient(message, socketIOClient)
    }

    private fun buildFoodInformationMessage(food: FoodInformation) =
        FoodInformationMessage(
            name = food.name,
            foodScore = food.reviewAverage,
            cost = food.cost,
            restaurantName = food.restaurantName,
            restaurantId = food.restaurantId,
            totalCount = food.totalAmount,
            rank = food.scoreRank
        )

    private fun getFoodEntity(foodId: Long) =
        foodInformationRepository.findByIdOrNull(foodId) ?: throw FoodNotFoundException.EXCEPTION

    private fun sendFoodInformationToClient(foodInformation: FoodInformationMessage, socketIOClient: SocketIOClient) =
        socketIOClient.sendEvent(FOOD_INFO_KEY, foodInformation)
}
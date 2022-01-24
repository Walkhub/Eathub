package com.eathub.eathub.domain.food.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.food.domain.repositories.FoodRepository
import com.eathub.eathub.domain.food.presentation.dto.CreateFoodMessage
import com.eathub.eathub.domain.food.presentation.dto.CreateFoodRequest
import com.eathub.eathub.domain.food.presentation.dto.FoodMessages
import com.eathub.eathub.domain.food.presentation.dto.FoodMessage
import com.eathub.eathub.domain.restaurant.domain.Restaurant
import com.eathub.eathub.domain.restaurant.domain.exportmanager.RestaurantExportManager
import com.eathub.eathub.domain.review.domain.Review
import org.springframework.stereotype.Service

@Service
class FoodService(
    private val socketIOServer: SocketIOServer,
    private val foodRepository: FoodRepository,
    private val restaurantExportManager: RestaurantExportManager
) {
    companion object {
        private const val CREATE_FOOD_KEY = "food.create"
        private const val FOOD_LIST_KEY = "food.list"
    }

    fun createNewFood(request: CreateFoodRequest) {
        val restaurant = restaurantExportManager.findRestaurantById(request.restaurantId)
        val food = buildFoodFromRequestAndRestaurant(request, restaurant)
        val savedFood = foodRepository.save(food)
        val message = buildFoodMessageFromRestaurantAndSavedFood(restaurant, savedFood)

        socketIOServer.broadcastOperations
            .sendEvent(CREATE_FOOD_KEY, message)
    }

    private fun buildFoodFromRequestAndRestaurant(request: CreateFoodRequest, restaurant: Restaurant): Food {
        return Food(
            name = request.name,
            cost = request.cost,
            picture = request.imageUrl,
            restaurant = restaurant
        )
    }

    private fun buildFoodMessageFromRestaurantAndSavedFood(restaurant: Restaurant, savedFood: Food): CreateFoodMessage {
        return CreateFoodMessage(
            restaurantId = restaurant.id,
            restaurantName = restaurant.name,
            name = savedFood.name,
            cost = savedFood.cost,
            foodId = savedFood.id,
            imageUrl = savedFood.picture
        )
    }

    fun getFoodList(socketIOClient: SocketIOClient) {
        val foods = foodRepository.findAllBy()
        val foodResponse = buildFoodListResponse(foods)

        socketIOClient.sendEvent(FOOD_LIST_KEY, foodResponse)
    }

    private fun buildFoodListResponse(foods: List<Food>): FoodMessages {
        val foodResponses = foods.map { buildFoodResponse(it) }
        return FoodMessages(foodResponses)
    }

    private fun buildFoodResponse(food: Food): FoodMessage {
        return FoodMessage(
            foodName = food.name,
            foodCost = food.cost,
            foodPicture = food.picture,
            foodScore = getRateAverage(food.review),
            restaurantName = food.restaurant.name
        )
    }

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

}
package com.eathub.eathub.domain.food.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.food.domain.repositories.FoodRepository
import com.eathub.eathub.domain.food.presentation.dto.CreateFoodMessage
import com.eathub.eathub.domain.food.presentation.dto.CreateFoodRequest
import com.eathub.eathub.domain.food.presentation.dto.FoodResponse
import com.eathub.eathub.domain.rate.domain.Rate
import com.eathub.eathub.domain.restaurant.domain.exportmanager.RestaurantExportManager
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

        val food = Food(
            name = request.name,
            cost = request.cost,
            picture = request.imageUrl,
            restaurant = restaurant
        )

        val savedFood = foodRepository.save(food)

        val message = CreateFoodMessage(
            restaurantId = restaurant.id,
            restaurantName = restaurant.name,
            name = savedFood.name,
            cost = savedFood.cost,
            foodId = savedFood.id,
            imageUrl = savedFood.picture
        )

        socketIOServer.broadcastOperations
            .sendEvent(CREATE_FOOD_KEY, message)
    }

    fun getFoodList(socketIOClient: SocketIOClient) {
        val foods = foodRepository.findAllBy()
        val foodResponse = foods.map { buildFoodListResponseFromFoodEntity(it) }

        socketIOClient.sendEvent(FOOD_LIST_KEY, foodResponse)
    }

    private fun buildFoodListResponseFromFoodEntity(food: Food): FoodResponse {
        return FoodResponse(
            foodName = food.name,
            foodCost = food.cost,
            foodPicture = food.picture,
            foodScore = getRateAverage(food.rate),
            restaurantName = food.restaurant.name
        )
    }

    private fun getRateAverage(rates: List<Rate>): Double? {
        val isRateExists = rates.isNotEmpty()
        val doubleAverage = rates.map { it.score }
            .average()

        val roundedAverage = String.format("%.2f", doubleAverage).toDouble()

        return when (isRateExists) {
            true -> roundedAverage
            false -> null
        }
    }

}
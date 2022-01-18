package com.eathub.eathub.domain.food.service

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.food.domain.repositories.FoodRepository
import com.eathub.eathub.domain.food.presentation.dto.CreateFoodRequest
import com.eathub.eathub.domain.restaurant.domain.exportmanager.RestaurantExportManager
import org.springframework.stereotype.Service

@Service
class FoodService(
    private val socketIOServer: SocketIOServer,
    private val foodRepository: FoodRepository,
    private val restaurantExportManager: RestaurantExportManager
) {
    fun createNewFood(request: CreateFoodRequest) {
        val restaurant = restaurantExportManager.findRestaurantById(request.restaurantId)

        val food = Food(
            name = request.name,
            cost = request.cost,
            picture = request.imageUrl,
            restaurant = restaurant
        )

        foodRepository.save(food)
    }

}
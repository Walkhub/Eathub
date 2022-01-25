package com.eathub.eathub.domain.food.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.food.domain.repositories.FoodRepository
import com.eathub.eathub.domain.food.exceptions.FoodNotFoundException
import com.eathub.eathub.domain.food.presentation.dto.*
import com.eathub.eathub.domain.restaurant.domain.Restaurant
import com.eathub.eathub.domain.restaurant.domain.exportmanager.RestaurantExportManager
import com.eathub.eathub.domain.review.domain.Review
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class FoodService(
    private val socketIOServer: SocketIOServer,
    private val foodRepository: FoodRepository,
    private val restaurantExportManager: RestaurantExportManager
) {

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
            foodName = savedFood.name,
            foodCost = savedFood.cost,
            foodId = savedFood.id,
            foodPicture = savedFood.picture
        )

    private fun sendCreateFoodMessageToAllClient(createFoodMessage: CreateFoodMessage) =
        socketIOServer.broadcastOperations
            .sendEvent(SocketProperties.CREATE_FOOD_KEY, createFoodMessage)

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
            restaurantName = food.restaurant.name,
            foodId = food.id
        )

    private fun sendFoodListToClient(foodMessages: FoodMessages, socketIOClient: SocketIOClient) =
        socketIOClient.sendEvent(SocketProperties.FOOD_LIST_KEY, foodMessages)

    fun getFoodInformation(request: FoodInformationRequest, socketIOClient: SocketIOClient) {
        val food = getFoodEntity(request.foodId)
        val message = buildFoodInformationMessage(food)

        sendFoodInformationToClient(message, socketIOClient)
        joinFoodInformationRoom(socketIOClient, request.foodId)
    }

    private fun buildFoodInformationMessage(food: Food) =
        FoodInformationMessage(
            name = food.name,
            cost = food.cost,
            restaurantName = food.restaurant.name,
            restaurantId = food.restaurant.id,
            imageUrl = food.picture
        )

    private fun getFoodEntity(foodId: Long) =
        foodRepository.findByIdOrNull(foodId) ?: throw FoodNotFoundException.EXCEPTION

    private fun sendFoodInformationToClient(foodInformation: FoodInformationMessage, socketIOClient: SocketIOClient) =
        socketIOClient.sendEvent(SocketProperties.FOOD_INFO_KEY, foodInformation)

    private fun joinFoodInformationRoom(socketIOClient: SocketIOClient, foodId: Long) =
        socketIOClient.joinRoom(getFoodRoomName(foodId))

    fun signOutRoom(socketIOClient: SocketIOClient, request: FoodSignOutRequest) {
        outRoom(socketIOClient, request.foodId)
    }

    private fun outRoom(socketIOClient: SocketIOClient, roomId: Long) =
        socketIOClient.leaveRoom(getFoodRoomName(roomId))

    private fun getFoodRoomName(foodId: Long) = "$SocketProperties.FOOD_INFO_ROOM_KEY$foodId"
}
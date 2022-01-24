package com.eathub.eathub.domain.food.presentation

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.annotation.OnEvent
import com.eathub.eathub.domain.food.presentation.dto.CreateFoodRequest
import com.eathub.eathub.domain.food.presentation.dto.FoodInformationRequest
import com.eathub.eathub.domain.food.service.FoodService
import org.springframework.web.bind.annotation.RestController

@RestController
class FoodController(
    private val foodService: FoodService
) {
    @OnEvent("/food/create")
    fun saveFood(request: CreateFoodRequest) {
        foodService.createNewFood(request)
    }

    @OnEvent("/food/list")
    fun getFoodList(socketIOClient: SocketIOClient) {
        foodService.getFoodList(socketIOClient)
    }

    @OnEvent("/food/information")
    fun getFoodList(socketIOClient: SocketIOClient, request: FoodInformationRequest) {
        foodService.getFoodInformation(request, socketIOClient)
    }

}
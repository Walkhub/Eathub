package com.eathub.eathub.domain.food.application.presentation

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.annotation.OnEvent
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationRequest
import com.eathub.eathub.domain.food.application.presentation.dto.GetFoodApplicationListRequest
import com.eathub.eathub.domain.food.application.presentation.dto.MyFoodApplicationRequest
import com.eathub.eathub.domain.food.application.service.FoodApplicationService
import org.springframework.web.bind.annotation.RestController

@RestController
class FoodApplicationController(
    private val foodApplicationService: FoodApplicationService
) {
    @OnEvent("/food/application")
    fun foodApplication(socketIOClient: SocketIOClient, request: FoodApplicationRequest) {
        foodApplicationService.createFoodApplication(request, socketIOClient)
    }

    @OnEvent("/food/application/list")
    fun foodApplicationList(socketIOClient: SocketIOClient, request: GetFoodApplicationListRequest) {
        foodApplicationService.getApplicationList(socketIOClient, request)
    }

    @OnEvent("/food/application/mine")
    fun myFoodApplicationList(socketIOClient: SocketIOClient, request: MyFoodApplicationRequest) {
        foodApplicationService.getMyFoodApplication(socketIOClient, request)
    }
}
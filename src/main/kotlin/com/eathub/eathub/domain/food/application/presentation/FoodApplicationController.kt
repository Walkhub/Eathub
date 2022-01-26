package com.eathub.eathub.domain.food.application.presentation

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.annotation.OnEvent
import com.eathub.eathub.domain.food.application.presentation.dto.FoodApplicationRequest
import com.eathub.eathub.domain.food.application.presentation.dto.GetFoodApplicationListRequest
import com.eathub.eathub.domain.food.application.service.FoodApplicationService
import org.springframework.web.bind.annotation.RestController

@RestController
class FoodApplicationController(
    private val foodApplicationService: FoodApplicationService
) {
    @OnEvent("/food/application")
    fun foodApplication(request: FoodApplicationRequest) {
        foodApplicationService.createFoodApplication(request)
    }

    @OnEvent("/food/application/list")
    fun foodApplication(socketIOClient: SocketIOClient, request: GetFoodApplicationListRequest) {
        foodApplicationService.getApplicationList(socketIOClient, request)
    }
}
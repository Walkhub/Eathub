package com.eathub.eathub.domain.food.presentation

import com.corundumstudio.socketio.annotation.OnEvent
import com.eathub.eathub.domain.food.presentation.dto.CreateFoodRequest
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

}
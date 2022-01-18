package com.eathub.eathub.domain.food.exportmanager

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.food.domain.repositories.FoodRepository
import com.eathub.eathub.domain.food.exceptions.FoodNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class FoodExportManager(
    private val foodRepository: FoodRepository
) {
    fun findFoodById(id: Long): Food {
        return foodRepository.findByIdOrNull(id) ?: throw FoodNotFoundException.EXCEPTION
    }
}
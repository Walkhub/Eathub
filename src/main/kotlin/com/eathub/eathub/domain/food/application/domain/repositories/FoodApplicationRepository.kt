package com.eathub.eathub.domain.food.application.domain.repositories

import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.FoodApplicationId
import org.springframework.data.jpa.repository.JpaRepository

interface FoodApplicationRepository : JpaRepository<FoodApplication, FoodApplicationId> {
}
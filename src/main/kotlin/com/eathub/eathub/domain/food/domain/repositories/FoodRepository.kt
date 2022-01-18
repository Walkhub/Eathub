package com.eathub.eathub.domain.food.domain.repositories

import com.eathub.eathub.domain.food.domain.Food
import org.springframework.data.repository.CrudRepository

interface FoodRepository : CrudRepository<Food, Long> {
}
package com.eathub.eathub.domain.food.domain.repositories

import com.eathub.eathub.domain.food.domain.Food
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FoodRepository : JpaRepository<Food, Long> {
    @Query("select f from Food f join fetch f.restaurant left join fetch f.review")
    fun findAllBy(): List<Food>

    fun findAllByIdIn(ids: List<Long>)
}
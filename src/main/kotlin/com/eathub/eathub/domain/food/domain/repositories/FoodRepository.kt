package com.eathub.eathub.domain.food.domain.repositories

import com.eathub.eathub.domain.food.domain.Food
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface FoodRepository : JpaRepository<Food, Long> {
    @Query("select distinct f from Food f join fetch f.restaurant left join fetch f.review")
    fun findAllBy(): List<Food>

    @Query("select distinct f from Food f join fetch f.application a join fetch a.food where f.id in :ids")
    fun findAllByIdIn(@Param("ids") ids: List<Long>): List<Food>
}
package com.eathub.eathub.domain.food.application.domain.repositories

import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.FoodApplicationId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface FoodApplicationRepository : JpaRepository<FoodApplication, FoodApplicationId> {
    @Query("select a from FoodApplication a join fetch a.food f join fetch f.restaurant left join fetch a.optionApplication oa left join fetch oa.option where (oa.option.id is null or a.food = oa.option.food) and a.applicationDate between :startDate and :endDate")
    fun findAllByApplicationDateBetween(@Param("startDate") startDate: LocalDateTime, @Param("endDate") endDate: LocalDateTime): List<FoodApplication>
}
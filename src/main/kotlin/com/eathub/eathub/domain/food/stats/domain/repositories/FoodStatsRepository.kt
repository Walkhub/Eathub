package com.eathub.eathub.domain.food.stats.domain.repositories

import com.eathub.eathub.domain.food.stats.domain.FoodStats
import com.eathub.eathub.domain.food.stats.domain.FoodStatsId
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface FoodStatsRepository : JpaRepository<FoodStats, FoodStatsId> {
    fun findAllByFoodStatsIdApplicationTypeAndFoodStatsIdApplicationDate(
        applicationType: ApplicationType,
        applicationDate: LocalDate
    ): List<FoodStats>
}
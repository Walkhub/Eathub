package com.eathub.eathub.global.facade

import com.eathub.eathub.domain.food.application.presentation.dto.FoodStatsMessage
import com.eathub.eathub.domain.user.domain.enums.ApplicationType

interface FoodStatsFacade {
    fun getFoodStats(applicationType: ApplicationType): FoodStatsMessage
    fun sendMoneyStatsToAllClient(message: FoodStatsMessage)
}
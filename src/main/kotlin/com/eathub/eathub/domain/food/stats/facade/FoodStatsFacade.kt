package com.eathub.eathub.domain.food.stats.facade

import com.eathub.eathub.domain.user.domain.enums.ApplicationType

sealed interface FoodStatsFacade {
    fun sendUsedAmountMessage(applicationType: ApplicationType)
}
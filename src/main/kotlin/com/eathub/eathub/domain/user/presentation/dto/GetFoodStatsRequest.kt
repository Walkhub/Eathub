package com.eathub.eathub.domain.user.presentation.dto

import com.eathub.eathub.domain.user.domain.enums.ApplicationType

class GetFoodStatsRequest(
    val applicationType: ApplicationType,
    val userName: String
)
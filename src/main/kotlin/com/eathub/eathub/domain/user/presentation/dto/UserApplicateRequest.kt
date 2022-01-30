package com.eathub.eathub.domain.user.presentation.dto

import com.eathub.eathub.domain.user.domain.enums.ApplicationType

class UserApplicateRequest(
    val applicationType: ApplicationType
)
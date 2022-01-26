package com.eathub.eathub.domain.user.domain.exportmanager

import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import com.eathub.eathub.domain.user.domain.repositories.ApplicationUserRepository
import com.eathub.eathub.domain.user.exceptions.UserNameNotFoundException
import org.springframework.stereotype.Component

@Component
class ApplicationUserExportManager(
    private val applicationUserRepository: ApplicationUserRepository
) {
    fun findByUserIdAndApplicationType(userName: String, applicationType: ApplicationType): ApplicationUser =
        applicationUserRepository.findByUserNameAndIdApplicationType(userName, applicationType)
            ?: throw UserNameNotFoundException.EXCEPTION

}
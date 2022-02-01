package com.eathub.eathub.domain.user.domain.exportmanager

import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import com.eathub.eathub.domain.user.domain.repositories.ApplicationUserRepository
import com.eathub.eathub.domain.user.exceptions.UserNameNotFoundException
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ApplicationUserExportManager(
    private val applicationUserRepository: ApplicationUserRepository
) {
    fun findByUserIdAndApplicationType(userName: String, applicationType: ApplicationType, applicationDate: LocalDate): ApplicationUser =
        applicationUserRepository.findByUserNameAndIdApplicationTypeAndIdApplicationDate(userName, applicationType, applicationDate)
            ?: throw UserNameNotFoundException.EXCEPTION

    fun findAllByApplicationDate(applicationDate: LocalDate) =
        applicationUserRepository.findAllByIdApplicationDate(applicationDate)

}
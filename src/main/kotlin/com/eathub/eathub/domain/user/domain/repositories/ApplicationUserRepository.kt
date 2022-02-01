package com.eathub.eathub.domain.user.domain.repositories

import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.ApplicationUserId
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ApplicationUserRepository : JpaRepository<ApplicationUser, ApplicationUserId> {
    fun findByUserNameAndIdApplicationTypeAndIdApplicationDate(userName: String, applicationType: ApplicationType, applicationDate: LocalDate): ApplicationUser?
    fun findAllByIdApplicationDate(applicationDate: LocalDate): List<ApplicationUser>
}
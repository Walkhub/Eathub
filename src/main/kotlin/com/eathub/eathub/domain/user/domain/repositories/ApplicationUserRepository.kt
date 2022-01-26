package com.eathub.eathub.domain.user.domain.repositories

import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.ApplicationUserId
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ApplicationUserRepository : JpaRepository<ApplicationUser, ApplicationUserId> {
    fun findByUserNameAndIdApplicationType(userName: String, applicationType: ApplicationType): ApplicationUser?

    @Query("select au from ApplicationUser au join fetch au.foodApplication fa join fetch OptionApplication oa join fetch fa.food f join fetch f.restaurant where au.id.applicationType = :type and au.id.applicationDate = :date")
    fun findAllByApplicationDateAndApplicationType(
        @Param("date") date: LocalDate,
        @Param("type") applicationType: ApplicationType
    ): List<ApplicationUser>
}
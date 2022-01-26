package com.eathub.eathub.domain.user.domain

import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class ApplicationUserId(
    @Column
    val userId: Long,

    @Column
    val applicationType: ApplicationType,

    @Column
    val applicationDate: LocalDate = LocalDate.now()
) : Serializable
package com.eathub.eathub.domain.food.stats.domain

import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class FoodStatsId(
    @Column
    val userId: Long,

    @Column
    val applicationType: ApplicationType,

    @Column
    val applicationDate: LocalDate
): Serializable
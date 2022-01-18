package com.eathub.eathub.domain.food.application.domain

import com.eathub.eathub.domain.food.application.domain.enums.ApplicationType
import java.time.LocalDate
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
class FoodApplication(
    @EmbeddedId
    val foodApplicationId: FoodApplicationId,
    val applicationType: ApplicationType,
    val applicationDate: LocalDate
)
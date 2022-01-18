package com.eathub.eathub.domain.food.application.domain

import com.eathub.eathub.domain.food.application.domain.enums.ApplicationType
import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class FoodApplication(
    @EmbeddedId
    val foodApplicationId: FoodApplicationId,

    @Enumerated(EnumType.STRING)
    @NotNull
    val applicationType: ApplicationType,

    @NotNull
    val applicationDate: LocalDate,

    @MapsId("foodId")
    @ManyToOne(fetch = FetchType.LAZY)
    val food: Food,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User
)
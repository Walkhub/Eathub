package com.eathub.eathub.domain.rate.domain

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Rate(
    @NotNull
    val score: Double,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @MapsId("foodId")
    @ManyToOne(fetch = FetchType.LAZY)
    val food: Food
) {
    @EmbeddedId
    val rateId: RateId = RateId(user.id, food.id)
}
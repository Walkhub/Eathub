package com.eathub.eathub.domain.rate.domain

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import javax.persistence.*

@Entity
class Rate(
    @EmbeddedId
    val rateId: RateId,

    val score: Double,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @MapsId("foodId")
    @ManyToOne(fetch = FetchType.LAZY)
    val food: Food
)
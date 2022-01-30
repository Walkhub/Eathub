package com.eathub.eathub.domain.food.stats.domain

import org.hibernate.annotations.Immutable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "used_amount")
@Immutable
class FoodStats(

    @EmbeddedId
    val foodStatsId: FoodStatsId,

    val usedAmount: Long,

    val userName: String,

    val deliveryFee: Long

)
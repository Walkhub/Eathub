package com.eathub.eathub.domain.food.application.domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class FoodApplicationId(
    @Column
    val foodId: Long,
    @Column
    val userId: Long
) : Serializable
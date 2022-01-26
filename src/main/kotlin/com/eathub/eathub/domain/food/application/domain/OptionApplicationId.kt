package com.eathub.eathub.domain.food.application.domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class OptionApplicationId(
    @Column
    val optionId: Long,

    @Column
    val foodApplicationId: Long
) : Serializable
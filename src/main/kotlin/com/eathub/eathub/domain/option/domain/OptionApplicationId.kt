package com.eathub.eathub.domain.option.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class OptionApplicationId(
    @Column
    val option_id: Long,

    @Column
    val food_application_id: Long
)
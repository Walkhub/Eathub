package com.eathub.eathub.domain.food.application.domain

import com.eathub.eathub.domain.option.domain.Option
import javax.persistence.*

@Entity
class OptionApplication(
    @MapsId("optionId")
    @ManyToOne(fetch = FetchType.LAZY)
    val option: Option,

    @MapsId("foodApplicationId")
    @ManyToOne(fetch = FetchType.LAZY)
    val foodApplication: FoodApplication
) {
    @EmbeddedId
    val optionApplicationId = OptionApplicationId(option.id, foodApplication.id)
}
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

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is OptionApplication -> other.optionApplicationId == optionApplicationId
            else -> false
        }
    }

    override fun hashCode(): Int {
        return "${optionApplicationId.foodApplicationId}${optionApplicationId.optionId}".toInt()
    }
}
package com.eathub.eathub.domain.option.domain

import com.eathub.eathub.domain.food.domain.Food
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Option(
    @NotNull
    val value: String,

    @NotNull
    val cost: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    val food: Food
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
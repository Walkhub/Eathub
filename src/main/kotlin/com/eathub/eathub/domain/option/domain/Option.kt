package com.eathub.eathub.domain.option.domain

import com.eathub.eathub.domain.food.domain.Food
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Option(
    @NotNull
    val value: String,

    @ManyToOne(fetch = FetchType.LAZY)
    val food: Food
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
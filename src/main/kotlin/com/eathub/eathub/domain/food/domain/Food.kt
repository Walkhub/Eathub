package com.eathub.eathub.domain.food.domain

import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.rate.domain.Rate
import com.eathub.eathub.domain.restaurant.domain.Restaurant
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Food(
    @NotNull
    val name: String,

    @NotNull
    val cost: Long,

    @NotNull
    val picture: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant,

    @OneToMany(mappedBy = "food", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val rate: List<Rate> = mutableListOf(),

    @OneToMany(mappedBy = "food", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val application: List<FoodApplication> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
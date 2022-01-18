package com.eathub.eathub.domain.restaurant.domain

import com.eathub.eathub.domain.food.domain.Food
import javax.persistence.*

@Entity
class Restaurant(
    val name: String,

    val deliveryFee: Long,

    @OneToMany(mappedBy = "restaurant", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val food: List<Food> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
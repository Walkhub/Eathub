package com.eathub.eathub.domain.restaurant.domain

import com.eathub.eathub.domain.food.domain.Food
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Restaurant(
    @NotNull
    @Column(unique = true, nullable = false)
    val name: String,

    @NotNull
    val deliveryFee: Long,

    @OneToMany(mappedBy = "restaurant", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val food: List<Food> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
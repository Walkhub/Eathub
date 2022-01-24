package com.eathub.eathub.domain.food.domain

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "food_information")
class FoodInformation(
    @Id
    val id: Long,
    val name: String,
    val cost: Long,
    val totalAmount: Int,
    val scoreRank: Int,
    val restaurantName: String,
    val restaurantId: Long,
    val reviewAverage: Double
)
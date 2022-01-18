package com.eathub.eathub.domain.user.domain

import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.rate.domain.Rate
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(indexes = [Index(name = "user_name_index", columnList = "name", unique = true)])
class User(
    @NotNull
    val name: String,

    @OneToMany(mappedBy = "user")
    val rate: List<Rate> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    val foodApplication: List<FoodApplication> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
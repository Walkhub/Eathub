package com.eathub.eathub.domain.food.application.domain

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.ApplicationUser
import javax.persistence.*

@Entity
class FoodApplication(
    val count: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    val food: Food,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "user_id", referencedColumnName = "user_id"),
        JoinColumn(name = "application_type", referencedColumnName = "applicationType"),
        JoinColumn(name = "application_date", referencedColumnName = "applicationDate")
    )
    val applicationUser: ApplicationUser,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "foodApplication", cascade = [CascadeType.PERSIST])
    val optionApplication: MutableList<OptionApplication> = mutableListOf()

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun addOptionApplication(optionApplication: OptionApplication) {
        this.optionApplication.add(optionApplication)
    }
}
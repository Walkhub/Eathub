package com.eathub.eathub.domain.user.domain

import com.eathub.eathub.domain.food.application.domain.FoodApplication
import javax.persistence.*

@Entity
class ApplicationUser(
    @EmbeddedId
    val id: ApplicationUserId,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @OneToMany(mappedBy = "applicationUser")
    val foodApplication: List<FoodApplication> = mutableListOf()
)
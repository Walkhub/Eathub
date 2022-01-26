package com.eathub.eathub.domain.user.domain

import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class ApplicationUser(
    @NotNull
    val applicationType: ApplicationType,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @OneToMany(mappedBy = "applicationUser")
    val foodApplication: List<FoodApplication> = mutableListOf()
) {
    @EmbeddedId
    val id: ApplicationUserId = ApplicationUserId(user.id, applicationType)
}
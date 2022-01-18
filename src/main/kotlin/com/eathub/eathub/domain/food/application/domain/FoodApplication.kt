package com.eathub.eathub.domain.food.application.domain

import com.eathub.eathub.domain.food.application.domain.enums.ApplicationType
import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotNull

@EntityListeners(AuditingEntityListener::class)
@Entity
class FoodApplication(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val applicationType: ApplicationType,

    @MapsId("foodId")
    @ManyToOne(fetch = FetchType.LAZY)
    val food: Food,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User
) {
    @CreatedDate
    @NotNull
    lateinit var applicationDate: LocalDate

    @EmbeddedId
    val foodApplicationId: FoodApplicationId = FoodApplicationId(food.id, user.id)
}
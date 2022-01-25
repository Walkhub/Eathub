package com.eathub.eathub.domain.review.domain

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Review(
    @NotNull
    val score: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    val food: Food,

    @NotNull
    val content: String
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @NotNull
    var createAt: LocalDateTime = LocalDateTime.now()
}
package com.eathub.eathub.domain.review.domain

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Review(
    @NotNull
    val score: Double,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @MapsId("foodId")
    @ManyToOne(fetch = FetchType.LAZY)
    val food: Food
) {
    @EmbeddedId
    val reviewId: ReviewId = ReviewId(user.id, food.id)
}
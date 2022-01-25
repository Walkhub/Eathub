package com.eathub.eathub.domain.review.domain

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
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
    val food: Food,

    val content: String
) {
    @EmbeddedId
    val reviewId: ReviewId = ReviewId(user.id, food.id)

    @NotBlank
    var createAt: LocalDateTime = LocalDateTime.now()
}
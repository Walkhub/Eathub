package com.eathub.eathub.domain.review.domain

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@EntityListeners(AuditingEntityListener::class)
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

    @CreatedDate
    @Column(nullable = false)
    var createAt: LocalDateTime? = null
        protected set
}
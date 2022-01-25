package com.eathub.eathub.domain.review.domain

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "review_information")
class ReviewInformation(
    @Id
    val id: Long,
    val totalAmount: Int,
    val scoreRank: Int,
    val content: String,
    val createAt: LocalDateTime,
    val reviewAverage: Double,
    val foodId: Long,
    val userId: Long,
    val userName: String,
    val score: Double
)
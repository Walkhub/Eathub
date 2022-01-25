package com.eathub.eathub.domain.review.domain

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
    val reviewAverage: Double
)
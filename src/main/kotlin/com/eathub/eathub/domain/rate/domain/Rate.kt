package com.eathub.eathub.domain.rate.domain

import javax.persistence.*

@Entity
class Rate(
    @EmbeddedId
    val rateId: RateId,
    val score: Double
)
package com.eathub.eathub.domain.review.domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class ReviewId(
    @Column
    val userId: Long,

    @Column
    val foodId: Long
) : Serializable
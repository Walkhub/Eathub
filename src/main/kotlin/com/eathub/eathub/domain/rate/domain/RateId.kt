package com.eathub.eathub.domain.rate.domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class RateId(
    @Column
    val userId: Long,

    @Column
    val foodId: Long
) : Serializable
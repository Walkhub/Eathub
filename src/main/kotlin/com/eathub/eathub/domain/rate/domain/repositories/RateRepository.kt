package com.eathub.eathub.domain.rate.domain.repositories

import com.eathub.eathub.domain.rate.domain.Rate
import com.eathub.eathub.domain.rate.domain.RateId
import org.springframework.data.repository.CrudRepository

interface RateRepository : CrudRepository<Rate, RateId> {
}
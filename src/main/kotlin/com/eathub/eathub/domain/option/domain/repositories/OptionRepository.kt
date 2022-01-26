package com.eathub.eathub.domain.option.domain.repositories

import com.eathub.eathub.domain.option.domain.Option
import org.springframework.data.jpa.repository.JpaRepository

interface OptionRepository : JpaRepository<Option, Long> {
    fun findAllByFoodId(id: Long): List<Option>
}
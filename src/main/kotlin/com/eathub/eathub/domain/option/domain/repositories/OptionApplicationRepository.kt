package com.eathub.eathub.domain.option.domain.repositories

import com.eathub.eathub.domain.option.domain.Option
import com.eathub.eathub.domain.option.domain.OptionApplicationId
import org.springframework.data.jpa.repository.JpaRepository

interface OptionApplicationRepository : JpaRepository<Option, OptionApplicationId>
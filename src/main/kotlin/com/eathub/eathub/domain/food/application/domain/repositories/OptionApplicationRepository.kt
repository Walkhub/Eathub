package com.eathub.eathub.domain.food.application.domain.repositories

import com.eathub.eathub.domain.food.application.domain.OptionApplicationId
import com.eathub.eathub.domain.option.domain.Option
import org.springframework.data.jpa.repository.JpaRepository

interface OptionApplicationRepository : JpaRepository<Option, OptionApplicationId>
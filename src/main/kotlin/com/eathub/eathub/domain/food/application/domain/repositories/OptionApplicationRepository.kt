package com.eathub.eathub.domain.food.application.domain.repositories

import com.eathub.eathub.domain.food.application.domain.OptionApplication
import com.eathub.eathub.domain.food.application.domain.OptionApplicationId
import org.springframework.data.jpa.repository.JpaRepository

interface OptionApplicationRepository : JpaRepository<OptionApplication, OptionApplicationId>
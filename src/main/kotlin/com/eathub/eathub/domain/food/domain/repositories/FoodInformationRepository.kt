package com.eathub.eathub.domain.food.domain.repositories

import com.eathub.eathub.domain.food.domain.FoodInformation
import org.springframework.data.jpa.repository.JpaRepository

interface FoodInformationRepository : JpaRepository<FoodInformation, Long> {
}
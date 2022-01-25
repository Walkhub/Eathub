package com.eathub.eathub.domain.restaurant.domain.repositories

import com.eathub.eathub.domain.restaurant.domain.Restaurant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, Long> {
}
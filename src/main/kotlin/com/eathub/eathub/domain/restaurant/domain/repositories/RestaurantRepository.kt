package com.eathub.eathub.domain.restaurant.domain.repositories

import com.eathub.eathub.domain.restaurant.domain.Restaurant
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : CrudRepository<Restaurant, Long> {
}
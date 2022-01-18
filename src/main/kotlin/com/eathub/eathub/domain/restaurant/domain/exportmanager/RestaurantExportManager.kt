package com.eathub.eathub.domain.restaurant.domain.exportmanager

import com.eathub.eathub.domain.restaurant.domain.Restaurant
import com.eathub.eathub.domain.restaurant.domain.repositories.RestaurantRepository
import com.eathub.eathub.domain.restaurant.exceptions.RestaurantNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class RestaurantExportManager(
    private val restaurantRepository: RestaurantRepository
) {
    fun findRestaurantById(id: Long): Restaurant {
        return restaurantRepository.findByIdOrNull(id) ?: throw RestaurantNotFoundException.EXCEPTION
    }

}
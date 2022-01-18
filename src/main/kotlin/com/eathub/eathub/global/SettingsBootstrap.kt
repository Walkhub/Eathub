package com.eathub.eathub.global

import com.eathub.eathub.domain.restaurant.domain.Restaurant
import com.eathub.eathub.domain.restaurant.domain.repositories.RestaurantRepository
import com.eathub.eathub.domain.user.domain.User
import com.eathub.eathub.domain.user.domain.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class SettingsBootstrap(
    private val userRepository: UserRepository,
    private val restaurantRepository: RestaurantRepository,

) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val names = listOf("김기영", "김범진", "김시안", "김의찬", "유현명", "신희원", "이용진", "이재원", "이준서", "임세현", "전세현", "전영준", "정대현", "정지우", "최민준", "한준호", "홍정현")
        val users = names.map { name -> User(name) }

        val restaurants = mutableListOf<Restaurant>()
        restaurants.add(Restaurant(name = "퀴즈노스", deliveryFee = 5000))
        restaurants.add(Restaurant(name = "롯데리아", deliveryFee = 3000))
        restaurants.add(Restaurant(name = "정환이 가득찬 집밥", deliveryFee = 3000))
        restaurants.add(Restaurant(name = "국수나무", deliveryFee = 3000))

        try {
            userRepository.saveAll(users)
        } catch (_: Exception) {}

        try {
            restaurantRepository.saveAll(restaurants)
        } catch (_: Exception) {}

    }

}
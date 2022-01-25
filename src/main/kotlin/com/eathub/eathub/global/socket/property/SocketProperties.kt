package com.eathub.eathub.global.socket.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.socketio")
class SocketProperties(
    val port: Int
) {
    companion object {
        const val ERROR: String = "error"
        const val FOOD_INFO_ROOM_KEY = "food.room."
        const val CREATE_FOOD_KEY = "food.create"
        const val FOOD_LIST_KEY = "food.list"
        const val FOOD_INFO_KEY = "food.information"
        const val CREATE_REVIEW_KEY = "review.create"
        const val GET_REVIEW_KEY = "review.list"
        const val FOOD_APPLICATION_KEY = "food.application"
        fun getFoodRoomName(foodId: Long) = "$SocketProperties.FOOD_INFO_ROOM_KEY$foodId"
    }
}
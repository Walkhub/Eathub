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
        private const val FOOD_INFO_ROOM_KEY = "food.room."
        private const val OPTION_ROOM_KEY = "option.room."
        private const val CREATE_OPTION_KEY = "option.create."
        const val OPTION_LIST_KEY = "option.list"
        const val CREATE_FOOD_KEY = "food.create"
        const val FOOD_LIST_KEY = "food.list"
        const val FOOD_INFO_KEY = "food.information"
        const val CREATE_REVIEW_KEY = "review.create"
        const val GET_REVIEW_KEY = "review.list"
        const val FOOD_APPLICATION_KEY = "food.application"
        fun getFoodRoomName(foodId: Long) = "$FOOD_INFO_ROOM_KEY$foodId"
        fun getOptionRoomName(foodId: Long) = "$OPTION_ROOM_KEY$foodId"
        fun getOptionResponseKey(foodId: Long) = "$CREATE_OPTION_KEY$foodId"
    }
}
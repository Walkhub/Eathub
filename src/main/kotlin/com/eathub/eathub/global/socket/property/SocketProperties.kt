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
        const val CHAT: String = "chat"
    }
}
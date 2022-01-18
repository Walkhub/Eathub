package com.eathub.eathub.global.socket

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketConfig
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner
import com.corundumstudio.socketio.protocol.JacksonJsonSupport
import com.eathub.eathub.global.socket.error.SocketErrorController
import com.eathub.eathub.global.socket.property.SocketProperties
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class SocketIOConfig(
    private val socketErrorController: SocketErrorController,
    private val socketProperties: SocketProperties
) {
    @Bean
    fun socketIOServer(): SocketIOServer {
        val customSocketConfig = SocketConfig()
        val customConfig = Configuration()

        customSocketConfig.apply {
            isReuseAddress = true
        }

        customConfig.apply {
            socketConfig = customSocketConfig
            port = socketProperties.port
            origin = "*"
            exceptionListener = socketErrorController
            jsonSupport = JacksonJsonSupport(kotlinModule())
        }

        return SocketIOServer(customConfig)
    }

    @Bean
    fun springAnnotationScanner(socketServer: SocketIOServer): SpringAnnotationScanner {
        return SpringAnnotationScanner(socketServer)
    }
}
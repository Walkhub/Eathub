package com.eathub.eathub.domain.user.domain.exportmanager

import com.corundumstudio.socketio.SocketIOClient
import com.eathub.eathub.domain.user.domain.User
import com.eathub.eathub.domain.user.domain.repositories.UserRepository
import com.eathub.eathub.domain.user.exceptions.UserNameNotFoundException
import com.eathub.eathub.global.socket.controller.ConnectController
import org.springframework.stereotype.Component

@Component
class UserExportManager(
    private val userRepository: UserRepository
) {
    fun findUserByName(name: String): User =
        userRepository.findByName(name) ?: throw UserNameNotFoundException.EXCEPTION

    fun getUserNameFromSocketIOClient(socketIOClient: SocketIOClient): String =
        socketIOClient.get(ConnectController.CLIENT_NAME_KEY)

}
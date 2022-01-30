package com.eathub.eathub.global.socket.controller

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.annotation.OnConnect
import org.springframework.stereotype.Controller

@Controller
class ConnectController {

    companion object {
        const val NAME_KEY = "name"
        const val CLIENT_NAME_KEY = "name"
    }

    @OnConnect
    fun onConnect(socketIOClient: SocketIOClient) {
        val name = getUserNameFromClient(socketIOClient)
        socketIOClient.set(CLIENT_NAME_KEY, name)
    }

    private fun getUserNameFromClient(socketIOClient: SocketIOClient) =
        socketIOClient.handshakeData.getSingleUrlParam(NAME_KEY)
}
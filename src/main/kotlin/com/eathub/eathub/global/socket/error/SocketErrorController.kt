package com.eathub.eathub.global.socket.error

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.listener.ExceptionListener
import com.eathub.eathub.global.exception.GlobalException
import com.eathub.eathub.global.exception.exceptions.InternalServerError
import com.eathub.eathub.global.payload.BaseResponse
import com.eathub.eathub.global.socket.property.SocketProperties
import io.netty.channel.ChannelHandlerContext
import org.springframework.stereotype.Controller

@Controller
class SocketErrorController : ExceptionListener {
    override fun onEventException(e: Exception, args: MutableList<Any>?, client: SocketIOClient) = doError(e, client)

    override fun onDisconnectException(e: Exception, client: SocketIOClient) = doError(e, client)

    override fun onConnectException(e: Exception, client: SocketIOClient) = doError(e, client)

    override fun onPingException(e: Exception, client: SocketIOClient) = doError(e, client)

    override fun exceptionCaught(ctx: ChannelHandlerContext, e: Throwable?): Boolean = false

    private fun doError(throwable: Throwable, client: SocketIOClient) {
        when (throwable) {
            is GlobalException -> sendErrorMessage(throwable, client)
            else -> sendErrorMessage(InternalServerError.EXCEPTION, client)
        }
    }

    private fun sendErrorMessage(e: GlobalException, client: SocketIOClient) {
        val errorResponse = BaseResponse.of(e)
        client.sendEvent(SocketProperties.ERROR, errorResponse)
    }
}
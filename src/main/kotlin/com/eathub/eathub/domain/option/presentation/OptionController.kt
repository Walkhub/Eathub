package com.eathub.eathub.domain.option.presentation

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.annotation.OnEvent
import com.eathub.eathub.domain.option.presentation.dto.CreateOptionRequest
import com.eathub.eathub.domain.option.presentation.dto.GetOptionListRequest
import com.eathub.eathub.domain.option.presentation.dto.JoinOptionRoomRequest
import com.eathub.eathub.domain.option.service.OptionService
import org.springframework.web.bind.annotation.RestController

@RestController
class OptionController(
    private val optionService: OptionService
) {
    @OnEvent("/option/create")
    fun createOption(request: CreateOptionRequest) {
        optionService.createOption(request)
    }

    @OnEvent("/option/join")
    fun joinOption(socketIOClient: SocketIOClient, request: JoinOptionRoomRequest) {
        optionService.joinOptionRoom(socketIOClient, request)
    }

    @OnEvent("/option/list")
    fun getOptionList(socketIOClient: SocketIOClient, request: GetOptionListRequest) {
        optionService.getOptionList(socketIOClient, request)
    }
}
package com.eathub.eathub.domain.user.presentation

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.annotation.OnEvent
import com.eathub.eathub.domain.user.presentation.dto.GetFoodStatsRequest
import com.eathub.eathub.domain.user.presentation.dto.UserApplicateRequest
import com.eathub.eathub.domain.user.service.ApplicationUserService
import org.springframework.web.bind.annotation.RestController

@RestController
class ApplicationUserController(
    private val applicationUserService: ApplicationUserService
) {

    @OnEvent("/user/application")
    fun saveApplicationUser(socketIOClient: SocketIOClient, request: UserApplicateRequest) {
        applicationUserService.applicateUser(socketIOClient, request)
    }

    @OnEvent("/money")
    fun getMoneyStats(request: GetFoodStatsRequest) {
        applicationUserService.getFoodStats(request)
    }
}
package com.eathub.eathub.domain.user.service

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.ApplicationUserId
import com.eathub.eathub.domain.user.domain.User
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import com.eathub.eathub.domain.user.domain.exportmanager.ApplicationUserExportManager
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import com.eathub.eathub.domain.user.domain.repositories.ApplicationUserRepository
import com.eathub.eathub.domain.user.domain.repositories.UserRepository
import com.eathub.eathub.domain.user.presentation.dto.UserApplicateMessage
import com.eathub.eathub.domain.user.presentation.dto.UserApplicateRequest
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.stereotype.Service

@Service
class ApplicationUserService(
    private val applicationUserRepository: ApplicationUserRepository,
    private val userExportManager: UserExportManager,
    private val socketIOServer: SocketIOServer
) {
    fun applicateUser(request: UserApplicateRequest) {
        val user = getUserByName(request)
        val applicationUser = buildApplicationUser(user, request.applicationType)
        val savedApplicationUser = saveApplicationUser(applicationUser)

        val applicateMessage = buildUserApplicateMessage(savedApplicationUser)
    }

    private fun getUserByName(request: UserApplicateRequest) =
        userExportManager.findUserByName(request.userName)

    private fun buildApplicationUser(user: User, type: ApplicationType) =
        ApplicationUser(
            user = user,
            id = ApplicationUserId(user.id, type)
        )

    private fun saveApplicationUser(applicationUser: ApplicationUser) =
        applicationUserRepository.save(applicationUser)

    private fun buildUserApplicateMessage(applicationUser: ApplicationUser) =
        UserApplicateMessage(
            applicationType = applicationUser.id.applicationType,
            userName = applicationUser.user.name
        )

    private fun sendApplicationUserMessageToClient(applicateMessage: UserApplicateMessage) =
        socketIOServer.broadcastOperations
            .sendEvent(SocketProperties.APPLICATE_USER_KEY, applicateMessage)
}
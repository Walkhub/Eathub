package com.eathub.eathub.domain.user.service

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.ApplicationUserId
import com.eathub.eathub.domain.user.domain.User
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import com.eathub.eathub.domain.user.domain.repositories.ApplicationUserRepository
import com.eathub.eathub.domain.user.exceptions.UserNameNotFoundException
import com.eathub.eathub.domain.user.presentation.dto.GetFoodStatsRequest
import com.eathub.eathub.domain.user.presentation.dto.UserApplicateMessage
import com.eathub.eathub.domain.user.presentation.dto.UserApplicateRequest
import com.eathub.eathub.global.facade.FoodStatsFacade
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ApplicationUserService(
    private val applicationUserRepository: ApplicationUserRepository,
    private val userExportManager: UserExportManager,
    private val foodStatsFacade: FoodStatsFacade,
    private val socketIOServer: SocketIOServer
) {
    @Transactional
    fun applicateUser(request: UserApplicateRequest) {
        val user = getUserByName(request)
        applicationUserRepository.findByIdOrNull(ApplicationUserId(user.id, request.applicationType, LocalDate.now()))?.let {
            throw UserNameNotFoundException.EXCEPTION
        }

        val applicationUser = buildApplicationUser(user, request.applicationType)
        val savedApplicationUser = saveApplicationUser(applicationUser)

        val applicateMessage = buildUserApplicateMessage(savedApplicationUser)
        sendApplicationUserMessageToAllClient(applicateMessage)
        sendStatsMessage(request.applicationType, request.userName)
    }

    @Transactional
    fun getFoodStats(request: GetFoodStatsRequest) {
        sendStatsMessage(request.applicationType, request.userName)
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

    private fun sendApplicationUserMessageToAllClient(applicateMessage: UserApplicateMessage) =
        socketIOServer.broadcastOperations
            .sendEvent(SocketProperties.APPLICATE_USER_KEY, applicateMessage)

    private fun sendStatsMessage(applicationType: ApplicationType, userName: String) {
        val foodStats = foodStatsFacade.getFoodStats(applicationType, userName)
        foodStatsFacade.sendMoneyStatsToAllClient(foodStats)
    }
}
package com.eathub.eathub.domain.food.stats.facade

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.stats.domain.FoodStats
import com.eathub.eathub.domain.food.stats.domain.repositories.FoodStatsRepository
import com.eathub.eathub.domain.food.stats.presentation.dto.FoodStatsMessage
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import com.eathub.eathub.domain.user.domain.exportmanager.ApplicationUserExportManager
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class FoodStatsFacadeImpl(
    private val foodStatsRepository: FoodStatsRepository,
    private val userExportManager: UserExportManager,
    private val socketIOServer: SocketIOServer,
    private val applicationUserExportManager: ApplicationUserExportManager
) : FoodStatsFacade {

    companion object {
        const val FOOD_STAT_KEY = "money"
        const val TOTAL_AMOUNT = 136000
    }

    @Transactional
    override fun sendUsedAmountMessage(applicationType: ApplicationType) {
        applicationUserExportManager.flush()
        val foodStats = getFoodStatsForNow(applicationType)
        socketIOServer.allClients
            .forEach { sendFoodStatsToClient(it, foodStats) }
    }

    private fun getFoodStatsForNow(applicationType: ApplicationType) =
        foodStatsRepository.findAllByFoodStatsIdApplicationTypeAndFoodStatsIdApplicationDate(
            applicationType,
            LocalDate.now()
        ).groupBy { it.userName }

    private fun sendFoodStatsToClient(socketIOClient: SocketIOClient, foodStats: Map<String, List<FoodStats>>) {
        val name = userExportManager.getUserNameFromSocketIOClient(socketIOClient)
        val users = applicationUserExportManager.findAllByApplicationDate(LocalDate.now())

        val totalUsedAmount = foodStats.values.sumOf { it.sumOf { foodStats -> foodStats.usedAmount } }
        val totalAmount = TOTAL_AMOUNT - (foodStats.values.firstOrNull()?.firstOrNull()?.deliveryFee ?: 0)

        val amountPerPerson = when (users.size) {
            0 -> totalAmount
            else -> totalAmount / users.size
        }
        val remainedAmount = totalAmount - totalUsedAmount

        foodStats[name]
            ?.map {
                FoodStatsMessage(
                    amountPerPerson = amountPerPerson,
                    usedAmount = it.usedAmount,
                    totalUsedAmount = totalUsedAmount,
                    remainedAmount = remainedAmount
                )
            }
            ?.forEach { socketIOClient.sendEvent(FOOD_STAT_KEY, it) }
    }

}
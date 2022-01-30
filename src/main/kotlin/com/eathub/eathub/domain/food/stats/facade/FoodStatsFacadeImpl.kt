package com.eathub.eathub.domain.food.stats.facade

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.stats.domain.FoodStats
import com.eathub.eathub.domain.food.stats.domain.repositories.FoodStatsRepository
import com.eathub.eathub.domain.food.stats.presentation.dto.FoodStatsMessage
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import com.eathub.eathub.domain.user.domain.exportmanager.UserExportManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class FoodStatsFacadeImpl(
    private val foodStatsRepository: FoodStatsRepository,
    private val userExportManager: UserExportManager,
    private val socketIOServer: SocketIOServer
) : FoodStatsFacade {

    companion object {
        const val FOOD_STAT_KEY = "money"
        const val TOTAL_AMOUNT = 136000
    }

    @Transactional
    override fun sendUsedAmountMessage(applicationType: ApplicationType) {
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
        foodStats[name]
            ?.map {  }
            ?.forEach { socketIOClient.sendEvent(FOOD_STAT_KEY, it) }
    }

    private fun buildFoodStatsMessage(foodStats: List<FoodStats>, totalUsedAmount: Long): FoodStatsMessage {
        val foodStats = foodStats.first()

        return FoodStatsMessage(
            usedAmount = foodStats.usedAmount,
            totalUsedAmount = foodStats.deliveryFee,
            myUsedAmount = foodStats.usedAmount
        ) }
    }
}
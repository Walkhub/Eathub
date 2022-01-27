package com.eathub.eathub.global.facade

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.presentation.dto.FoodStatsMessage
import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import com.eathub.eathub.domain.user.domain.repositories.ApplicationUserRepository
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.lang.ArithmeticException
import java.time.LocalDate

@Component
class FoodStatsFacadeImpl(
    private val applicationUserRepository: ApplicationUserRepository,
    private val socketIOServer: SocketIOServer
) : FoodStatsFacade {

    companion object {
        const val TOTAL_AMOUNT = 136000
    }

    @Transactional
    override fun getFoodStats(applicationType: ApplicationType): FoodStatsMessage {
        val applicationUsers = getApplicationUsers(applicationType)
        val usedAmount = getUsedAmount(applicationUsers)
        val amountPerPerson = getAmountPerPerson(applicationUsers)
        val remainedAmount = getRemainedAmount(usedAmount)

        return buildFoodStatsMessage(
            usedAmount = usedAmount,
            amountPerPerson = amountPerPerson,
            remainedAmount = remainedAmount
        )
    }

    private fun getApplicationUsers(applicationType: ApplicationType) =
        applicationUserRepository.findAllByApplicationDateAndApplicationType(LocalDate.now(), applicationType)

    private fun getUsedAmount(applicationUsers: List<ApplicationUser>) =
        applicationUsers.sumOf { getSumOfAmountFromFoodApplications(it.foodApplication) }

    private fun getSumOfAmountFromFoodApplications(foodApplication: List<FoodApplication>): Long {
        return foodApplication.sumOf { it.food.cost } + getTotalDeliveryFee(foodApplication)
    }

    private fun getTotalDeliveryFee(foodApplication: List<FoodApplication>) =
        foodApplication.sumOf { it.food.restaurant.deliveryFee }

    private fun getAmountPerPerson(applicationUsers: List<ApplicationUser>) =
        try {
            TOTAL_AMOUNT.div(applicationUsers.size)
        } catch (e: ArithmeticException) {
            TOTAL_AMOUNT
        }

    private fun getRemainedAmount(usedAmount: Long) =
        TOTAL_AMOUNT - usedAmount

    private fun buildFoodStatsMessage(usedAmount: Long, amountPerPerson: Int, remainedAmount: Long) =
        FoodStatsMessage(
            usedAmount = usedAmount,
            amountPerPerson = amountPerPerson,
            remainedAmount = remainedAmount
        )

    override fun sendMoneyStatsToAllClient(message: FoodStatsMessage) =
        socketIOServer.broadcastOperations
            .sendEvent(SocketProperties.MONEY_KEY, message)
}
package com.eathub.eathub.global.facade

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.OptionApplication
import com.eathub.eathub.domain.food.application.presentation.dto.FoodStatsMessage
import com.eathub.eathub.domain.user.domain.ApplicationUser
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import com.eathub.eathub.domain.user.domain.repositories.ApplicationUserRepository
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
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
        val deliveryFee = getTotalDeliveryFee(applicationUsers)
        val amountCanUse = getAmountCanUse(applicationUsers)
        val amountPerPerson = getAmountPerPerson(amountCanUse, applicationType)
        val remainedAmount =
            getRemainedAmountFromAmountCanUseAndDeliveryFeeAndUsedAmount(amountCanUse, deliveryFee, usedAmount)

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
        return foodApplication.sumOf { it.food.cost } + foodApplication.sumOf { getSumOfAmountFromOptionApplications(it.optionApplication) }
    }

    private fun getSumOfAmountFromOptionApplications(optionApplication: List<OptionApplication>) =
        optionApplication.sumOf { it.option.cost }

    private fun getAmountPerPerson(amountCanUse: Long, applicationType: ApplicationType) =
        try {
            val applicationUserCount =
                applicationUserRepository.countByIdApplicationDateAndIdApplicationType(LocalDate.now(), applicationType)
            amountCanUse.div(applicationUserCount)
        } catch (e: ArithmeticException) {
            amountCanUse
        }

    private fun getAmountCanUse(applicationUsers: List<ApplicationUser>) =
        TOTAL_AMOUNT - getTotalDeliveryFee(applicationUsers)

    private fun getRemainedAmountFromAmountCanUseAndDeliveryFeeAndUsedAmount(
        amountCanUse: Long,
        deliveryFee: Long,
        usedAmount: Long
    ) =
        amountCanUse - deliveryFee - usedAmount

    private fun getTotalDeliveryFee(applicationUsers: List<ApplicationUser>) =
        applicationUsers.sumOf { applicationUser -> applicationUser.foodApplication.sumOf { it.food.restaurant.deliveryFee } }

    private fun buildFoodStatsMessage(usedAmount: Long, amountPerPerson: Long, remainedAmount: Long) =
        FoodStatsMessage(
            usedAmount = usedAmount,
            amountPerPerson = amountPerPerson,
            remainedAmount = remainedAmount
        )

    override fun sendMoneyStatsToAllClient(message: FoodStatsMessage) =
        socketIOServer.broadcastOperations
            .sendEvent(SocketProperties.MONEY_KEY, message)
}
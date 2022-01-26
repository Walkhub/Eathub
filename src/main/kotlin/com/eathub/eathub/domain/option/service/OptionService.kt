package com.eathub.eathub.domain.option.service

import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.option.domain.Option
import com.eathub.eathub.domain.option.domain.repositories.OptionRepository
import com.eathub.eathub.domain.option.presentation.dto.CreateOptionMessage
import com.eathub.eathub.domain.option.presentation.dto.CreateOptionRequest
import com.eathub.eathub.global.socket.property.SocketProperties
import org.springframework.stereotype.Service

@Service
class OptionService(
    private val optionRepository: OptionRepository,
    private val foodExportManager: FoodExportManager,
    private val socketIOServer: SocketIOServer
) {
    fun createOption(request: CreateOptionRequest) {
        val option = buildOption(request)
        val savedOption = saveOption(option)

        val message = buildCreateOptionMessage(savedOption)
        sendCreateOptionMessage(request.foodId, message)
    }

    private fun buildOption(request: CreateOptionRequest): Option {
        val food = findFoodById(request.foodId)
        return Option(
            value = request.optionName,
            food = food,
            cost = request.optionCost
        )
    }

    private fun findFoodById(id: Long) = foodExportManager.findFoodById(id)

    private fun saveOption(option: Option) = optionRepository.save(option)

    private fun buildCreateOptionMessage(option: Option) =
        CreateOptionMessage(
            optionId = option.id,
            optionName = option.value,
            optionCost = option.cost
        )

    private fun sendCreateOptionMessage(foodId: Long, message: CreateOptionMessage) =
        socketIOServer.getRoomOperations(SocketProperties.getOptionRoomName(foodId))
            .sendEvent(SocketProperties.CREATE_OPTION_KEY, message)
}
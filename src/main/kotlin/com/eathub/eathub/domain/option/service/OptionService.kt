package com.eathub.eathub.domain.option.service

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.eathub.eathub.domain.food.exportmanager.FoodExportManager
import com.eathub.eathub.domain.option.domain.Option
import com.eathub.eathub.domain.option.domain.repositories.OptionRepository
import com.eathub.eathub.domain.option.presentation.dto.*
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
        socketIOServer.getRoomOperations(getOptionRoomName(foodId))
            .sendEvent(SocketProperties.getOptionResponseKey(foodId), message)

    private fun getOptionRoomName(foodId: Long) = SocketProperties.getOptionRoomName(foodId)

    fun getOptionList(socketIOClient: SocketIOClient, request: GetOptionListRequest) {
        val options = getOptions(request.foodId)
        val message = buildGetOptionMessages(options)

        sendOptionMessagesToClient(socketIOClient, message)
        clientJoinOptionRoom(socketIOClient, request.foodId)
    }

    private fun getOptions(foodId: Long) =
        optionRepository.findAllByFoodId(foodId)

    private fun buildGetOptionMessages(options: List<Option>): GetOptionMessages {
        val getOptionMessageList = options.map { buildGetOptionMessage(it) }
        return GetOptionMessages(getOptionMessageList)
    }

    private fun buildGetOptionMessage(option: Option) =
        GetOptionMessage(
            optionCost = option.cost,
            optionName = option.value,
            optionId = option.id
        )

    private fun sendOptionMessagesToClient(socketIOClient: SocketIOClient, message: GetOptionMessages) =
        socketIOClient.sendEvent(SocketProperties.OPTION_LIST_KEY, message)

    private fun clientJoinOptionRoom(socketIOClient: SocketIOClient, foodId: Long) =
        socketIOClient.joinRoom(getOptionRoomName(foodId))

}
package com.eathub.eathub.domain.option.presentation.dto

class GetOptionMessages(
    val options: List<GetOptionMessage>
)
class GetOptionMessage(
    val optionId: Long,
    val optionName: String,
    val optionCost: Long
)
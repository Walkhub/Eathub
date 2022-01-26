package com.eathub.eathub.domain.option.domain.exportmanager

import com.eathub.eathub.domain.option.domain.repositories.OptionRepository
import org.springframework.stereotype.Component

@Component
class OptionExportManager(
    private val optionRepository: OptionRepository
) {

    fun findOptionsByIds(ids: List<Long>) =
        optionRepository.findAllByIdIn(ids)
}
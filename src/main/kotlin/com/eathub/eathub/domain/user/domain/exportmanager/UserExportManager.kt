package com.eathub.eathub.domain.user.domain.exportmanager

import com.eathub.eathub.domain.user.domain.User
import com.eathub.eathub.domain.user.domain.repositories.UserRepository
import com.eathub.eathub.domain.user.exceptions.UserIdNotFoundException
import com.eathub.eathub.domain.user.exceptions.UserNameNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class UserExportManager(
    private val userRepository: UserRepository
) {
    fun findUserByName(name: String): User {
        return userRepository.findByName(name) ?: throw UserNameNotFoundException.EXCEPTION
    }

    fun findUserById(id: Long): User {
        return userRepository.findByIdOrNull(id) ?: throw UserIdNotFoundException.EXCEPTION
    }
}
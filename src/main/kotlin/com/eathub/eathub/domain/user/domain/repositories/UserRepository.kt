package com.eathub.eathub.domain.user.domain.repositories

import com.eathub.eathub.domain.user.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
}
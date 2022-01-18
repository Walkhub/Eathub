package com.eathub.eathub.domain.user.domain

import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "user_name_index", columnList = "name", unique = true)])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String
)
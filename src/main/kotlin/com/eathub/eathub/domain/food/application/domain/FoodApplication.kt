package com.eathub.eathub.domain.food.application.domain

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class FoodApplication(
    val count: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    val food: Food,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "foodApplication", cascade = [CascadeType.PERSIST])
    val optionApplication: MutableList<OptionApplication> = mutableListOf()
) {
    @NotNull
    val applicationDate: LocalDateTime = LocalDateTime.now()

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun addOptionApplication(optionApplication: OptionApplication) {
        this.optionApplication.add(optionApplication)
    }
}
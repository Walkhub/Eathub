package com.eathub.eathub.domain.food.application.domain

import com.eathub.eathub.domain.food.application.domain.enums.ApplicationType
import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.user.domain.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotNull

@EntityListeners(AuditingEntityListener::class)
@Entity
class FoodApplication(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val applicationType: ApplicationType,

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
    @CreatedDate
    @NotNull
    lateinit var applicationDate: LocalDate

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun addOptionApplication(optionApplication: OptionApplication) {
        this.optionApplication.add(optionApplication)
    }
}
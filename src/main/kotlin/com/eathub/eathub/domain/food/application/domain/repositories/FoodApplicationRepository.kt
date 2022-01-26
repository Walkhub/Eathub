package com.eathub.eathub.domain.food.application.domain.repositories

import com.eathub.eathub.domain.food.application.domain.FoodApplication
import com.eathub.eathub.domain.food.application.domain.FoodApplicationId
import com.eathub.eathub.domain.user.domain.enums.ApplicationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface FoodApplicationRepository : JpaRepository<FoodApplication, FoodApplicationId> {
    @Query("select a from FoodApplication a join fetch a.food f join fetch f.restaurant left join fetch a.optionApplication oa left join fetch oa.option where (oa.option.id is null or a.food = oa.option.food) and a.applicationUser.id.applicationType = :applicationType and a.applicationUser.id.applicationDate = :date")
    fun findAllByApplicationType(
        @Param("applicationType") applicationType: ApplicationType,
        @Param("date") date: LocalDate
    ): List<FoodApplication>

    @Query("select a from FoodApplication a join fetch a.food f join fetch f.restaurant left join fetch a.optionApplication oa left join fetch oa.option where (oa.option.id is null or a.food = oa.option.food) and a.applicationUser.user.name = :userName and a.applicationUser.id.applicationType = :applicationType and a.applicationUser.id.applicationDate = :date")
    fun findAllByApplicationTypeAndUserName(
        @Param("applicationType") applicationType: ApplicationType,
        @Param("userName") userName: String,
        @Param("date") date: LocalDate
    ): List<FoodApplication>
}
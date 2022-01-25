package com.eathub.eathub.domain.review.domain.repositories

import com.eathub.eathub.domain.review.domain.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ReviewRepository : JpaRepository<Review, Long> {

    @Query("select r from Review r join fetch r.food join fetch r.user where r.food.id = :foodId")
    fun findAllReviewsByFoodId(@Param("foodId") foodId: Long): List<Review>

}
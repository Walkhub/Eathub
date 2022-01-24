package com.eathub.eathub.domain.review.domain.repositories

import com.eathub.eathub.domain.food.domain.Food
import com.eathub.eathub.domain.review.domain.Review
import com.eathub.eathub.domain.review.domain.ReviewId
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ReviewRepository : CrudRepository<Review, ReviewId> {

    @Query("select r from Review r join fetch r.food join fetch r.user where r.food = :food")
    fun findAllReviewsByFoodId(@Param("food") food: Food): List<Review>

}
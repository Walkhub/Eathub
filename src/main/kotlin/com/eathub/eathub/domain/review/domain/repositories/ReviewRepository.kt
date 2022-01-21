package com.eathub.eathub.domain.review.domain.repositories

import com.eathub.eathub.domain.review.domain.Review
import com.eathub.eathub.domain.review.domain.ReviewId
import org.springframework.data.repository.CrudRepository

interface ReviewRepository : CrudRepository<Review, ReviewId> {
}
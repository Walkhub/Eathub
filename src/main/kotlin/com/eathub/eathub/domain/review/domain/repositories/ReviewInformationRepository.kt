package com.eathub.eathub.domain.review.domain.repositories

import com.eathub.eathub.domain.review.domain.ReviewInformation
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewInformationRepository : JpaRepository<ReviewInformation, Long>
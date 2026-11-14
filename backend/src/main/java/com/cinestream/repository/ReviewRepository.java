package com.cinestream.repository;

import com.cinestream.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMediaIdOrderByCreatedAtDesc(Long mediaId);

    Optional<Review> findByUserIdAndMediaId(Long userId, Long mediaId);

    long countByMediaId(Long mediaId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.media.id = :mediaId")
    Double calculateAverageRatingForMedia(@Param("mediaId") Long mediaId);

    @Query("SELECT r.media.id, AVG(r.rating), COUNT(r.id) FROM Review r GROUP BY r.media.id")
    List<Object[]> findAverageRatingsAndCountsGrouped();
}

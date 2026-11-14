package com.cinestream.repository;

import com.cinestream.model.Rental;
import com.cinestream.model.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByUserIdOrderByStartDateDesc(Long userId);

    List<Rental> findByUserIdAndStatusOrderByStartDateDesc(Long userId, RentalStatus status);

    Optional<Rental> findFirstByUserIdAndMediaIdAndStatusAndEndDateAfter(
            Long userId, Long mediaId, RentalStatus status, LocalDateTime now);

    long countByStatus(RentalStatus status);

    @Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Rental r")
    BigDecimal sumTotalRevenue();
}

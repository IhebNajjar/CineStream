package com.cinestream.repository;

import com.cinestream.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findByMediaIdOrderBySeasonNumberAsc(Long mediaId);
    Optional<Season> findByMediaIdAndSeasonNumber(Long mediaId, Integer seasonNumber);
}

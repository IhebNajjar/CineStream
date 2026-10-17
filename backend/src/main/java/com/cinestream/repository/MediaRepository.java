package com.cinestream.repository;

import com.cinestream.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {

    @Query("SELECT DISTINCT m FROM Media m " +
           "LEFT JOIN FETCH m.genres " +
           "LEFT JOIN FETCH m.directors " +
           "WHERE m.id = :id")
    Optional<Media> findByIdWithGenresAndDirectors(@Param("id") Long id);
}

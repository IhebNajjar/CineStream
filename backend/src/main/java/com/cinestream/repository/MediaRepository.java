package com.cinestream.repository;

import com.cinestream.model.Media;
import com.cinestream.model.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {

    @Query("SELECT DISTINCT m FROM Media m " +
           "LEFT JOIN FETCH m.genres " +
           "LEFT JOIN FETCH m.directors " +
           "WHERE m.id = :id")
    Optional<Media> findByIdWithGenresAndDirectors(@Param("id") Long id);

    @Query("SELECT DISTINCT m FROM Media m JOIN m.mediaActors ma WHERE ma.actor.id = :actorId ORDER BY m.releaseYear DESC")
    List<Media> findByActorId(@Param("actorId") Long actorId);

    @Query("SELECT DISTINCT m FROM Media m JOIN m.directors d WHERE d.id = :directorId ORDER BY m.releaseYear DESC")
    List<Media> findByDirectorId(@Param("directorId") Long directorId);

    long countByType(MediaType type);
}

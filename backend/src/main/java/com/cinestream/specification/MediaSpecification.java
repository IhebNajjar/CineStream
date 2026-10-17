package com.cinestream.specification;

import com.cinestream.model.Genre;
import com.cinestream.model.Media;
import com.cinestream.model.MediaActor;
import com.cinestream.model.MediaType;
import com.cinestream.model.Director;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MediaSpecification {

    public static Specification<Media> filterMedia(
            String search,
            MediaType type,
            Long genreId,
            Long actorId,
            Long directorId,
            Integer year) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Avoid duplicate rows when joining collection tables
            if (query != null) {
                query.distinct(true);
            }

            // 1. Search by title (case-insensitive LIKE)
            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("title")), pattern));
            }

            // 2. Filter by media type (FILM / SERIE)
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            // 3. Filter by release year
            if (year != null && year > 0) {
                predicates.add(cb.equal(root.get("releaseYear"), year));
            }

            // 4. Filter by genre
            if (genreId != null && genreId > 0) {
                Join<Media, Genre> genreJoin = root.join("genres");
                predicates.add(cb.equal(genreJoin.get("id"), genreId));
            }

            // 5. Filter by actor
            if (actorId != null && actorId > 0) {
                Join<Media, MediaActor> mediaActorJoin = root.join("mediaActors");
                predicates.add(cb.equal(mediaActorJoin.get("actor").get("id"), actorId));
            }

            // 6. Filter by director
            if (directorId != null && directorId > 0) {
                Join<Media, Director> directorJoin = root.join("directors");
                predicates.add(cb.equal(directorJoin.get("id"), directorId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

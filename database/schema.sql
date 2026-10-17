-- CineStream Database Schema - Sprint 1
-- Database: cinestream_db

CREATE DATABASE IF NOT EXISTS cinestream_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cinestream_db;

-- 1. Table Genre
DROP TABLE IF EXISTS media_genre;
DROP TABLE IF EXISTS media_actor;
DROP TABLE IF EXISTS media_director;
DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS actor;
DROP TABLE IF EXISTS director;

CREATE TABLE genre (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Table Media
CREATE TABLE media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    release_year SMALLINT NOT NULL,
    type ENUM('FILM', 'SERIE') NOT NULL,
    duration INT NULL,
    language VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    minimum_age TINYINT NOT NULL DEFAULT 0,
    poster_url VARCHAR(500) NULL,
    trailer_url VARCHAR(500) NULL,
    rental_price_per_day DECIMAL(8,2) NOT NULL,
    CONSTRAINT chk_rental_price CHECK (rental_price_per_day > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Table Media_Genre (Association N-N)
CREATE TABLE media_genre (
    media_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (media_id, genre_id),
    CONSTRAINT fk_mg_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    CONSTRAINT fk_mg_genre FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Table Actor
CREATE TABLE actor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    bio TEXT NULL,
    birth_date DATE NULL,
    photo_url VARCHAR(500) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Table Media_Actor (Association N-N avec attribut character_name)
CREATE TABLE media_actor (
    media_id BIGINT NOT NULL,
    actor_id BIGINT NOT NULL,
    character_name VARCHAR(150) NOT NULL,
    PRIMARY KEY (media_id, actor_id),
    CONSTRAINT fk_ma_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    CONSTRAINT fk_ma_actor FOREIGN KEY (actor_id) REFERENCES actor(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Table Director
CREATE TABLE director (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    bio TEXT NULL,
    photo_url VARCHAR(500) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Table Media_Director (Association N-N)
CREATE TABLE media_director (
    media_id BIGINT NOT NULL,
    director_id BIGINT NOT NULL,
    PRIMARY KEY (media_id, director_id),
    CONSTRAINT fk_md_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    CONSTRAINT fk_md_director FOREIGN KEY (director_id) REFERENCES director(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance on filters & search
CREATE INDEX idx_media_title ON media(title);
CREATE INDEX idx_media_type ON media(type);
CREATE INDEX idx_media_release_year ON media(release_year);

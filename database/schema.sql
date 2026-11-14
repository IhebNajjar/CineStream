-- CineStream Database Schema - Full 4 Sprints
-- Database: cinestream_db

CREATE DATABASE IF NOT EXISTS cinestream_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cinestream_db;

-- Drop dependent tables first
DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS rental;
DROP TABLE IF EXISTS wallet_transaction;
DROP TABLE IF EXISTS wallet;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS episode;
DROP TABLE IF EXISTS season;
DROP TABLE IF EXISTS media_genre;
DROP TABLE IF EXISTS media_actor;
DROP TABLE IF EXISTS media_director;
DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS actor;
DROP TABLE IF EXISTS director;

-- 1. Table Genre
CREATE TABLE genre (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Table Media (avec video_key pour streaming Cloudflare R2)
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
    video_key VARCHAR(255) NULL,
    rental_price_per_day DECIMAL(8,2) NOT NULL,
    CONSTRAINT chk_rental_price CHECK (rental_price_per_day > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Table Season (US012, US034)
CREATE TABLE season (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    media_id BIGINT NOT NULL,
    season_number INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    release_year SMALLINT NULL,
    CONSTRAINT fk_season_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    UNIQUE KEY uk_media_season (media_id, season_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Table Episode (US012, US034)
CREATE TABLE episode (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    season_id BIGINT NOT NULL,
    episode_number INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NULL,
    duration INT NULL,
    video_key VARCHAR(255) NULL,
    CONSTRAINT fk_episode_season FOREIGN KEY (season_id) REFERENCES season(id) ON DELETE CASCADE,
    UNIQUE KEY uk_season_episode (season_id, episode_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Table Media_Genre (Association N-N)
CREATE TABLE media_genre (
    media_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (media_id, genre_id),
    CONSTRAINT fk_mg_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    CONSTRAINT fk_mg_genre FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Table Actor
CREATE TABLE actor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    bio TEXT NULL,
    birth_date DATE NULL,
    photo_url VARCHAR(500) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Table Media_Actor (Association N-N avec attribut character_name)
CREATE TABLE media_actor (
    media_id BIGINT NOT NULL,
    actor_id BIGINT NOT NULL,
    character_name VARCHAR(150) NOT NULL,
    PRIMARY KEY (media_id, actor_id),
    CONSTRAINT fk_ma_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    CONSTRAINT fk_ma_actor FOREIGN KEY (actor_id) REFERENCES actor(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Table Director
CREATE TABLE director (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    bio TEXT NULL,
    photo_url VARCHAR(500) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Table Media_Director (Association N-N)
CREATE TABLE media_director (
    media_id BIGINT NOT NULL,
    director_id BIGINT NOT NULL,
    PRIMARY KEY (media_id, director_id),
    CONSTRAINT fk_md_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    CONSTRAINT fk_md_director FOREIGN KEY (director_id) REFERENCES director(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Table Users (Sprint 3: US001, US002, US038, US039)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11. Table Wallet (Sprint 3: US001, US020, US021)
CREATE TABLE wallet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_wallet_balance CHECK (balance >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. Table Wallet_Transaction (Sprint 3 & 4: US021, US022, US024)
CREATE TABLE wallet_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    type ENUM('RECHARGE', 'LOCATION') NOT NULL,
    description VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wt_wallet FOREIGN KEY (wallet_id) REFERENCES wallet(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 13. Table Rental (Sprint 4: US024, US026, US027, US028)
CREATE TABLE rental (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    days INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status ENUM('EN_COURS', 'TERMINEE') NOT NULL DEFAULT 'EN_COURS',
    CONSTRAINT fk_rental_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_rental_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 14. Table Review (Sprint 4: US016, US017, US018, US019)
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_media_review (user_id, media_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 15. Table Favorite (Sprint 4: US030, US031)
CREATE TABLE favorite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    added_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_fav_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_fav_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_media_fav (user_id, media_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for performance on filters, search & relationships
CREATE INDEX idx_media_title ON media(title);
CREATE INDEX idx_media_type ON media(type);
CREATE INDEX idx_media_release_year ON media(release_year);
CREATE INDEX idx_season_media ON season(media_id);
CREATE INDEX idx_episode_season ON episode(season_id);
CREATE INDEX idx_rental_user_status ON rental(user_id, status);
CREATE INDEX idx_rental_end_date ON rental(end_date);
CREATE INDEX idx_review_media ON review(media_id);
CREATE INDEX idx_fav_user ON favorite(user_id);

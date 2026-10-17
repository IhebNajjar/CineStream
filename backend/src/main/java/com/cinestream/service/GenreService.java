package com.cinestream.service;

import com.cinestream.dto.GenreDto;
import com.cinestream.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreDto> getAllGenres() {
        return genreRepository.findAllByOrderByNameAsc().stream()
                .map(g -> new GenreDto(g.getId(), g.getName(), g.getDescription()))
                .collect(Collectors.toList());
    }
}

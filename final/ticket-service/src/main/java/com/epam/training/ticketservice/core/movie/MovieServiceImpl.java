package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll()
                .stream()
                .map(this::mapEntityToDto)
                .toList();
    }

    @Override
    public void createMovie(MovieDto movieDto) {
        Movie movie = new Movie(
                movieDto.getName(),
                movieDto.getGenre(),
                movieDto.getLength()
        );

        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(MovieDto movieDto) {
        if (movieRepository.findByName(movieDto.getName())
                .isPresent()) {
            Movie movie = movieRepository.findByName(movieDto.getName()).get();
            movie.setGenre(movieDto.getGenre());
            movie.setLength(movieDto.getLength());

            movieRepository.save(movie);
        } else {
            // movie does not exists
        }
    }

    @Override
    public void deleteMovie(String name) {
        if (movieRepository.findByName(name).isPresent()) {
            Movie movie = movieRepository.findByName(name).get();
            movieRepository.delete(movie);
        } else {
            // movie does not exists
        }
    }

    private MovieDto mapEntityToDto(Movie movie) {
        return MovieDto.builder()
                .withName(movie.getName())
                .withGenre(movie.getGenre())
                .withLength(movie.getLength())
                .build();
    }

    private Optional<MovieDto> mapEntityToDto(Optional<Movie> movie) {
        return movie.map(this::mapEntityToDto);
    }

}

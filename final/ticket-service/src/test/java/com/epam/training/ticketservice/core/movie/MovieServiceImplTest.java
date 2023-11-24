package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

public class MovieServiceImplTest {

    private static final Movie ENTITY = new Movie("Sátántangó", "drama", 450);
    private static final MovieDto DTO = new MovieDto.Builder()
            .withName("Sátántangó")
            .withGenre("drama")
            .withLength(450)
            .build();

    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final MovieServiceImpl underTest = new MovieServiceImpl(movieRepository);

    @Test
    void testCreateMovieShouldSaveMovieWhenMovieIsNotExisting() {
        when(movieRepository.findByName(ENTITY.getName())).thenReturn(Optional.empty());
        when(movieRepository.save(ENTITY)).thenReturn(ENTITY);

        underTest.createMovie(DTO);

        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void testCreateMovieShouldNotSaveMovieWhenMovieIsExisting() {
        when(movieRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));

        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testUpdateMovieShouldUpdateMovieWhenMovieIsExisting() {
        when(movieRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));
        when(movieRepository.save(ENTITY)).thenReturn(ENTITY);

        underTest.updateMovie(DTO);

        verify(movieRepository).save(ENTITY);
    }

    @Test
    void testUpdateMovieShouldNotUpdateMovieWhenMovieIsNotExisting() {
        when(movieRepository.findByName(ENTITY.getName())).thenReturn(Optional.empty());

        underTest.updateMovie(DTO);

        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testDeleteMovieShouldDeleteMovieWhenMovieIsExisting() {
        when(movieRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));

        underTest.deleteMovie(ENTITY.getName());

        verify(movieRepository).delete(ENTITY);
    }

    @Test
    void testDeleteMovieShouldNotDeleteMovieWhenMovieIsNotExisting() {
        when(movieRepository.findByName(ENTITY.getName())).thenReturn(Optional.empty());

        underTest.deleteMovie(ENTITY.getName());

        verify(movieRepository, never()).delete(ENTITY);
    }

    @Test
    void testGetMovieListShouldReturnTheAvailableMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(ENTITY));

        List<MovieDto> actual = underTest.getMovieList();

        verify(movieRepository).findAll();
        assertEquals(1, actual.size());
    }

    @Test
    void testMovieDtoToStringMethodShouldReturnString() {
        when(movieRepository.findAll()).thenReturn(List.of(ENTITY));
        List<MovieDto> expected = underTest.getMovieList();

        List<MovieDto> actual = underTest.getMovieList();

        assertEquals(expected.toString(), actual.toString());
    }
}

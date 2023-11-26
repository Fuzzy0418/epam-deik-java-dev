package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.Shell;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("it")
public class MovieCommandIT {

    private static final MovieDto MOVIE_DTO = MovieDto.builder()
            .withName("Film1")
            .withGenre("drama")
            .withLength(300)
            .build();

    @Autowired
    private Shell shell;

    @SpyBean
    private MovieService movieService;

    @Test
    void testCreateMovieCommandShouldNotSaveTheMovieWhenUserIsNotLoggedInAsAdmin() {
        shell.evaluate(() -> "create movie Film1 drama 300");

        verify(movieService, times(0)).createMovie(MOVIE_DTO);
    }

    @Test
    void testCreateMovieCommandShoulSaveTheMovieWhenUserIsLoggedAsAdmin() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create movie Film1 drama 300");

        verify(movieService).createMovie(MOVIE_DTO);
        assertTrue(movieService.getMovieList().contains(MOVIE_DTO));
    }

    @Test
    void testListMovieShouldReturnListWhenListIsNotEmpty() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create movie Film1 drama 500");
        shell.evaluate(() -> "list movies");

        assertEquals(1, movieService.getMovieList().size());
    }
}

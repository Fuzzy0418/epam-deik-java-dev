package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistance.User;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class MovieCommand {

    private final MovieService movieService;
    private final UserService userService;

    @ShellMethod(key = "list movies", value = "List the available movies.")
    public String listMovie() {
        List<MovieDto> movieList = movieService.getMovieList();

        if (movieList.isEmpty()) {
            return "There are no movies at the moment";
        } else {
            String moviesAsString = movieList.stream()
                    .map(MovieDto::toString)
                    .collect(Collectors.joining("\n"));
            return moviesAsString;
        }
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "create movie", value = "Create a new movie.")
    public MovieDto createMovie(String name, String genre, int length) {
        MovieDto movieDto = MovieDto.builder()
                .withName(name)
                .withGenre(genre)
                .withLength(length)
                .build();

        movieService.createMovie(movieDto);
        return movieDto;
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "update movie", value = "Update the movie")
    public MovieDto updateMovie(String name, String genre, int length) {
        MovieDto movieDto = MovieDto.builder()
                .withName(name)
                .withGenre(genre)
                .withLength(length)
                .build();

        movieService.updateMovie(movieDto);
        return movieDto;
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "delete movie", value = "Delete the movie")
    public void deleteMovie(String name) {
        movieService.deleteMovie(name);
    }

    private Availability isAdmin() {
        Optional<UserDto> user = userService.describe();

        return user.isPresent() && user.get().role() == User.Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}

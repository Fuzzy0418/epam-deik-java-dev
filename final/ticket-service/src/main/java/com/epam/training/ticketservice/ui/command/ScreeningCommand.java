package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistance.User;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class ScreeningCommand {

    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final UserService userService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "create screening", value = "Create a new screening")
    public String createScreening(String movieName, String roomName, String startTime) {
        Screening screening = screeningService.createScreening(movieName, roomName,
                LocalDateTime.parse(startTime, formatter));
        return screening.toString();

        /*ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovie(movieName)
                .withRoom(roomName)
                .withStartTime(startTime)
                .build();

        screeningService.createScreening(screeningDto);
        return screeningDto;*/
    }

    /*@ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "delete screening", value = "Delete a specified screening")
    public void deleteScreening(String movieName, String roomName, LocalDateTime startTime) {
        screeningService.deleteScreening(asd);
    }*/

    private Availability isAdmin() {
        Optional<UserDto> user = userService.describe();

        return user.isPresent() && user.get().role() == User.Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}

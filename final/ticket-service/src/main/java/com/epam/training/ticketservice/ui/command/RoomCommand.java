package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
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
public class RoomCommand {

    private final RoomService roomService;
    private final UserService userService;

    @ShellMethod(key = "list rooms", value = "List the available rooms.")
    public String listRooms() {
        List<RoomDto> roomList = roomService.getRoomList();

        if (roomList.isEmpty()) {
            return "There are no rooms at the moment";
        } else {
            String roomsAsString = roomList.stream()
                    .map(RoomDto::toString)
                    .collect(Collectors.joining("\n"));
            return roomsAsString;
        }
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "create room", value = "Create a new room")
    public RoomDto createRoom(String name, int chairRows, int chairColumns) {
        RoomDto roomDto = RoomDto.builder()
                .withName(name)
                .withChairRows(chairRows)
                .withChairColumns(chairColumns)
                .build();

        roomService.createRoom(roomDto);
        return roomDto;
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "update room", value = "Update the specified room")
    public RoomDto updateRoom(String name, int chairRows, int chairColumns) {
        RoomDto roomDto = RoomDto.builder()
                .withName(name)
                .withChairRows(chairRows)
                .withChairColumns(chairColumns)
                .build();

        roomService.updateRoom(roomDto);
        return roomDto;
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "delete room", value = "Delete the specified room")
    public void deleteRoom(String name) {
        roomService.deleteRoom(name);
    }

    private Availability isAdmin() {
        Optional<UserDto> user = userService.describe();

        return user.isPresent() && user.get().role() == User.Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}

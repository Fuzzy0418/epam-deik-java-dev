package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

public class ScreeningServiceImplTest {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final Screening ENTITY = new Screening(
            new Movie("Film1", "drama", 300),
            new Room("Room1", 10, 10),
            LocalDateTime.parse("2023-11-24 10:40", formatter)
    );
    private static final ScreeningDto DTO = new ScreeningDto.Builder()
            .withMovie(new Movie("Film1", "drama", 300))
            .withRoom(new Room("Room1", 10, 10))
            .withStartTime(LocalDateTime.parse("2023-11-24 10:40", formatter))
            .build();

    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final ScreeningServiceImpl underTest = new ScreeningServiceImpl(screeningRepository, movieRepository, roomRepository);

    @Test
    void testCreateScreeningShouldSaveScreeningWhenScreeningIsNotExists() {
        when(movieRepository.findByName(ENTITY.getMovie().getName())).thenReturn(Optional.of(ENTITY.getMovie()));
        when(roomRepository.findByName(ENTITY.getRoom().getName())).thenReturn(Optional.of(ENTITY.getRoom()));
        when(screeningRepository.findScreeningByMovieAndRoomAndStartTime(ENTITY.getMovie(), ENTITY.getRoom(), ENTITY.getStartTime())).thenReturn(Optional.empty());

        underTest.createScreening(ENTITY.getMovie().getName(), ENTITY.getRoom().getName(), ENTITY.getStartTime());

        verify(screeningRepository).save(any(Screening.class));
    }

    @Test
    void testListScreeningsShouldReturnTheAvailableScreenings() {
        when(screeningRepository.findAll()).thenReturn(List.of(ENTITY));

        List<ScreeningDto> actual = underTest.listScreenings();

        verify(screeningRepository).findAll();
        assertEquals(1, actual.size());
    }

    @Test
    void testScreeningDtoToStringMethodShouldReturnString() {
        when(screeningRepository.findAll()).thenReturn(List.of(ENTITY));
        List<ScreeningDto> expected =  underTest.listScreenings();

        List<ScreeningDto> actual = underTest.listScreenings();

        assertEquals(expected.toString(), actual.toString());
    }
}

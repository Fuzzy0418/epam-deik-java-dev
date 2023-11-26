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
    private static final Screening ENTITY_2 = new Screening(
            new Movie("Film2", "animation", 300),
            new Room("Room1", 10, 10),
            LocalDateTime.parse("2023-11-24 10:45", formatter)
    );
    private static final Screening ENTITY_3 = new Screening(
            new Movie("Film3", "sci-fi", 300),
            new Room("Room1", 10, 10),
            LocalDateTime.parse("2023-11-24 15:45", formatter)
    );
    private static final Screening ENTITY_4 = new Screening(
            new Movie("Film4", "sci-fi", 300),
            new Room("Room1", 10, 10),
            LocalDateTime.parse("2023-11-24 14:40", formatter)
    );

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
    void testCreateScreeningShouldNotSaveScreeningWhenScreeningIsExists() {
        when(movieRepository.findByName(ENTITY.getMovie().getName())).thenReturn(Optional.of(ENTITY.getMovie()));
        when(roomRepository.findByName(ENTITY.getRoom().getName())).thenReturn(Optional.of(ENTITY.getRoom()));
        when(screeningRepository.findScreeningByMovieAndRoomAndStartTime(ENTITY.getMovie(), ENTITY.getRoom(), ENTITY.getStartTime())).thenReturn(Optional.of(ENTITY));

        underTest.createScreening(ENTITY.getMovie().getName(), ENTITY.getRoom().getName(), ENTITY.getStartTime());

        verify(screeningRepository).save(any(Screening.class));
    }

    @Test
    void testCreateScreeningShouldNotSaveScreeningWhenScreeningCoverAnotherScreening() {
        when(movieRepository.findByName(ENTITY_2.getMovie().getName())).thenReturn(Optional.of(ENTITY_2.getMovie()));
        when(roomRepository.findByName(ENTITY_2.getRoom().getName())).thenReturn(Optional.of(ENTITY_2.getRoom()));
        when(movieRepository.findByName(ENTITY_4.getMovie().getName())).thenReturn(Optional.of(ENTITY_4.getMovie()));
        when(roomRepository.findByName(ENTITY_4.getRoom().getName())).thenReturn(Optional.of(ENTITY_4.getRoom()));
        when(screeningRepository.findScreeningByRoom(ENTITY_2.getRoom())).thenReturn(Optional.of(ENTITY));

        String result = underTest.createScreening(ENTITY_2.getMovie().getName(), ENTITY_2.getRoom().getName(), ENTITY_2.getStartTime());
        String result_2 = underTest.createScreening(ENTITY_4.getMovie().getName(), ENTITY_4.getRoom().getName(), ENTITY_4.getStartTime());


        verify(screeningRepository, never()).save(any(Screening.class));
        assertEquals(result, "There is an overlapping screening");
        assertEquals(result_2, "There is an overlapping screening");
    }

    @Test
    void testCreateScreeningShouldNotSaveScreeningWhenScreeningOverlapsBreakTimeanotherScreening() {
        when(movieRepository.findByName(ENTITY_3.getMovie().getName())).thenReturn(Optional.of(ENTITY_3.getMovie()));
        when(roomRepository.findByName(ENTITY_3.getRoom().getName())).thenReturn(Optional.of(ENTITY_3.getRoom()));
        when(screeningRepository.findScreeningByRoom(ENTITY_3.getRoom())).thenReturn(Optional.of(ENTITY));

        String result = underTest.createScreening(ENTITY_3.getMovie().getName(), ENTITY_3.getRoom().getName(), ENTITY_3.getStartTime());

        verify(screeningRepository, never()).save(any(Screening.class));
        assertEquals(result, "This would start in the break period after another screening in this room");
    }

    @Test
    void testDeleteScreeningShouldDeleteWhenScreeningIsExists() {
        when(movieRepository.findByName(ENTITY.getMovie().getName())).thenReturn(Optional.of(ENTITY.getMovie()));
        when(roomRepository.findByName(ENTITY.getRoom().getName())).thenReturn(Optional.of(ENTITY.getRoom()));
        when(screeningRepository.findScreeningByMovieAndRoomAndStartTime(ENTITY.getMovie(), ENTITY.getRoom(), ENTITY.getStartTime())).thenReturn(Optional.of(ENTITY));

        underTest.deleteScreening(ENTITY.getMovie().getName(), ENTITY.getRoom().getName(), ENTITY.getStartTime());

        verify(screeningRepository).delete(any(Screening.class));
    }

    @Test
    void testDeleteScreeningShouldNotDeleteWhenScreeningIsNotExists() {
        when(movieRepository.findByName(ENTITY.getMovie().getName())).thenReturn(Optional.of(ENTITY.getMovie()));
        when(roomRepository.findByName(ENTITY.getRoom().getName())).thenReturn(Optional.of(ENTITY.getRoom()));
        when(screeningRepository.findScreeningByMovieAndRoomAndStartTime(ENTITY.getMovie(), ENTITY.getRoom(), ENTITY.getStartTime())).thenReturn(Optional.empty());

        underTest.deleteScreening(ENTITY.getMovie().getName(), ENTITY.getRoom().getName(), ENTITY.getStartTime());

        verify(screeningRepository, never()).delete(any(Screening.class));
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

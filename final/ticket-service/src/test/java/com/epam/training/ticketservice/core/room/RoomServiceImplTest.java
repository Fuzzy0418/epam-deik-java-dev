package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

public class RoomServiceImplTest {

    private static final Room ENTITY = new Room("Room 1", 10, 10);
    private static final RoomDto DTO = new RoomDto.Builder()
            .withName("Room 1")
            .withChairRows(10)
            .withChairColumns(10)
            .build();

    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final RoomServiceImpl underTest = new RoomServiceImpl(roomRepository);

    @Test
    void testCreateRoomShouldSaveRoomWhenRoomIsNotExisting() {
        when(roomRepository.findByName(ENTITY.getName())).thenReturn(Optional.empty());
        when(roomRepository.save(ENTITY)).thenReturn(ENTITY);

        underTest.createRoom(DTO);

        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void testCreateRoomShouldNotSaveRoomWhenRoomIsExisting() {
        when(roomRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));

        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void testUpdateRoomShouldUpdateRoomWhenRoomIsExisting() {
        when(roomRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));
        when(roomRepository.save(ENTITY)).thenReturn(ENTITY);

        underTest.updateRoom(DTO);

        verify(roomRepository).save(ENTITY);
    }

    @Test
    void testUpdateRoomShouldNotUpdateRoomWhenRoomIsNotExisting() {
        when(roomRepository.findByName(ENTITY.getName())).thenReturn(Optional.empty());

        underTest.updateRoom(DTO);

        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void testDeleteRoomShouldDeleteRoomWhenRoomIsExisting() {
        when(roomRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));

        underTest.deleteRoom(ENTITY.getName());

        verify(roomRepository).delete(ENTITY);
    }

    @Test
    void testDeleteRoomShouldNotDeleteRoomWhenRoomIsNotExisting() {
        when(roomRepository.findByName(ENTITY.getName())).thenReturn(Optional.empty());

        underTest.deleteRoom(ENTITY.getName());

        verify(roomRepository, never()).delete(ENTITY);
    }

    @Test
    void testGetRoomListShouldReturnTheAvailableRooms() {
        when(roomRepository.findAll()).thenReturn(List.of(ENTITY));

        List<RoomDto> actual = underTest.getRoomList();

        verify(roomRepository).findAll();
        assertEquals(1, actual.size());
    }

    @Test
    void testRoomDtoToStringMethodShouldReturnString() {
        when(roomRepository.findAll()).thenReturn(List.of(ENTITY));
        List<RoomDto> expected = underTest.getRoomList();

        List<RoomDto> actual = underTest.getRoomList();

        assertEquals(expected.toString(), actual.toString());
    }
}

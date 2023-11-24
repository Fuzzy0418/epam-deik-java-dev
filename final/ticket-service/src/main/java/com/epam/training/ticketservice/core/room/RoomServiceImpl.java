package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapEntityToDto)
                .toList();
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        Room room = new Room(
                roomDto.getName(),
                roomDto.getChairRows(),
                roomDto.getChairColumns()
        );

        roomRepository.save(room);
    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        if (roomRepository.findByName(roomDto.getName())
                .isPresent()) {
            Room room = roomRepository.findByName(roomDto.getName()).get();
            room.setChairRows(roomDto.getChairRows());
            room.setChairColumns(roomDto.getChairColumns());

            roomRepository.save(room);
        } else {
            // does not exists
        }
    }

    @Override
    public void deleteRoom(String name) {
        if (roomRepository.findByName(name).isPresent()) {
            Room room = roomRepository.findByName(name).get();
            roomRepository.delete(room);
        } else {
            //does not exists
        }
    }

    private RoomDto mapEntityToDto(Room room) {
        return RoomDto.builder()
                .withName(room.getName())
                .withChairRows(room.getChairRows())
                .withChairColumns(room.getChairColumns())
                .build();
    }

    private Optional<RoomDto> mapEntityToDto(Optional<Room> room) {
        return room.map(this::mapEntityToDto);
    }
}

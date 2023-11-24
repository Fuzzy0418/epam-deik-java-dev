package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<ScreeningDto> listScreenings() {
        return screeningRepository.findAll()
                .stream()
                .map(this::mapEntityToDto)
                .toList();
    }

    @Override
    public Screening createScreening(String movieName, String roomName, LocalDateTime startTime) {
        Optional<Movie> movie = movieRepository.findByName(movieName);
        Optional<Room> room = roomRepository.findByName(roomName);
        Optional<Screening> roomScreeningList = screeningRepository.findScreeningByRoom(room.get());

        Screening newScreening = new Screening(
            movie.get(),
            room.get(),
            startTime
        );

        LocalDateTime newScreeningStart = newScreening.getStartTime();
        LocalDateTime newScreeningEnd = newScreeningStart.plusMinutes(newScreening.getMovie().getLength());
        LocalDateTime newScreeningEndPlusBreakTime = newScreeningEnd.plusMinutes(10);

        boolean canScreening = true;

        /*for (Screening screening : roomScreeningList.stream().toList()) {
            LocalDateTime screeningStart = screening.getStartTime();
            LocalDateTime screeningEnd = screeningStart.plusMinutes(screening.getMovie().getLength());
            LocalDateTime screeningEndPlusBreakTime = screeningEnd.plusMinutes(10);

            //Kezdeti időpont bármelyik más adás start-end közé esik
            if (newScreeningStart.isAfter(screeningStart) && newScreeningStart.isBefore(screeningEnd)) {
                canScreening = false;
            //Befejező időpontja bármelyik másik adás startjától nagyobb
            } else if (newScreeningEnd.isAfter(screeningStart)) {
                canScreening = false;
            //Befejező időpontja + 10 perces szünetidő bármelyik adás startjától nagyobb
            } else if (newScreeningEndPlusBreakTime.isAfter(screeningStart)) {
                canScreening = false;
            //Bele esik egy másik adás utáni 10 perces szünetbe. (Kezdeti dátuma bármelyik adás breakPeriodtól kisebb)
            } else if (newScreeningStart.isBefore(screeningEndPlusBreakTime)) {
                canScreening = false;
            }
        }*/

        if (roomScreeningList.isEmpty() || !canScreening) {
            screeningRepository.save(newScreening);
        }

        return newScreening;

    }

    @Override
    public void deleteScreening(ScreeningDto screeningDto) {

    }

    private ScreeningDto mapEntityToDto(Screening screening) {
        return ScreeningDto.builder()
                .withMovie(screening.getMovie())
                .withRoom(screening.getRoom())
                .build();
    }

    private Optional<ScreeningDto> mapEntityToDto(Optional<Screening> screening) {
        return screening.map(this::mapEntityToDto);
    }
}

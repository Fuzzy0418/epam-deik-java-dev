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
    public String createScreening(String movieName, String roomName, LocalDateTime startTime) {
        Optional<Movie> movie = movieRepository.findByName(movieName);
        Optional<Room> room = roomRepository.findByName(roomName);

        Screening newScreening = new Screening(
            movie.get(),
            room.get(),
            startTime
        );

        String result = canCreateScreening(newScreening);

        if (result == "can") {
            screeningRepository.save(newScreening);
        } else {
            return result;
        }

        return null;
    }

    @Override
    public String deleteScreening(String movieName, String roomName, LocalDateTime startTime) {
        Optional<Movie> movie = movieRepository.findByName(movieName);
        Optional<Room> room = roomRepository.findByName(roomName);

        Optional<Screening> screening = screeningRepository
                .findScreeningByMovieAndRoomAndStartTime(movie.get(), room.get(), startTime);

        if (screening.isEmpty()) {
            return "The given screening does not exists";
        }

        screeningRepository.delete(screening.get());
        return null;
    }

    private String canCreateScreening(Screening newScreening) {
        Optional<Screening> roomScreeningList = screeningRepository.findScreeningByRoom(newScreening.getRoom());

        LocalDateTime newScreeningStart = newScreening.getStartTime();
        LocalDateTime newScreeningEnd = newScreeningStart.plusMinutes(newScreening.getMovie().getLength());
        LocalDateTime newScreeningEndPlusBreakTime = newScreeningEnd.plusMinutes(10);

        for (Screening screening : roomScreeningList.stream().toList()) {
            LocalDateTime screeningStart = screening.getStartTime();
            LocalDateTime screeningEnd = screeningStart.plusMinutes(screening.getMovie().getLength());
            LocalDateTime screeningEndPlusBreakTime = screeningEnd.plusMinutes(10);

            //Kezdeti időpont bármelyik más adás start-end közé esik
            if ((newScreeningStart.isAfter(screeningStart)
                    && newScreeningStart.isBefore(screeningEnd))
                    || newScreeningStart.isAfter(screeningStart) && newScreeningEnd.isBefore(screeningEnd)) {
                return "There is an overlapping screening";
                //Befejező időpontja bármelyik másik adás startjától nagyobb
            } else if (newScreeningEnd.isAfter(screeningStart)
                    && newScreeningEnd.isBefore(screeningEnd)) {
                return "There is an overlapping screening";
                //Befejező időpontja + 10 perces szünetidő bármelyik adás startjától nagyobb
            } else if (newScreeningStart.isAfter(screeningEnd)
                    && newScreeningStart.isBefore(screeningEndPlusBreakTime)) {
                return "This would start in the break period after another screening in this room";
                //Bele esik egy másik adás utáni 10 perces szünetbe. (Kezdeti dátuma bármelyik adás
                // breakPeriodtól kisebb)
            } else if (newScreeningEnd.isBefore(screeningStart)
                    && newScreeningEndPlusBreakTime.isAfter(screeningStart)) {
                return "This would start in the break period after another screening in this room";
            }
        }

        return "can";
    }

    private ScreeningDto mapEntityToDto(Screening screening) {
        return ScreeningDto.builder()
                .withMovie(screening.getMovie())
                .withRoom(screening.getRoom())
                .withStartTime(screening.getStartTime())
                .build();
    }

    private Optional<ScreeningDto> mapEntityToDto(Optional<Screening> screening) {
        return screening.map(this::mapEntityToDto);
    }
}

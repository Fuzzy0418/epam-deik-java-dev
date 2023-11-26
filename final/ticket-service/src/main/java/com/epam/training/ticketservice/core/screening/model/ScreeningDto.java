package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.room.persistence.Room;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Value
public class ScreeningDto {

    private final Movie movie;
    private final Room room;
    private final LocalDateTime startTime;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Movie movie;
        private Room room;
        private LocalDateTime startTime;

        public Builder withMovie(Movie movie) {
            this.movie = movie;
            return this;
        }

        public Builder withRoom(Room room) {
            this.room = room;
            return this;
        }

        public Builder withStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ScreeningDto build() {
            return new ScreeningDto(movie, room, startTime);
        }

    }

    @Override
    public String toString() {
        return movie.toMovieDto().toString() + ", screened in room " + room.getName() + ", at "
                + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}

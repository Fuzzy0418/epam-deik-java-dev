package com.epam.training.ticketservice.core.movie.model;

import lombok.Value;

@Value
public class MovieDto {

    private final String name;
    private final String genre;
    private final int length;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String genre;
        private int length;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder withLength(int length) {
            this.length = length;
            return this;
        }

        public MovieDto build() {
            return new MovieDto(name, genre, length);
        }
    }

    @Override
    public String toString() {
        return name + " (" + genre + ", " + length + " minutes)";
    }
}

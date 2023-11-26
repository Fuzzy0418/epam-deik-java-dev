package com.epam.training.ticketservice.core.movie.persistence;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name;

    private String genre;

    private int length;

    public Movie(String name, String genre, int length) {
        this.name = name;
        this.genre = genre;
        this.length = length;
    }

    public MovieDto toMovieDto() {
        MovieDto movieDto = new MovieDto.Builder()
                .withName(this.name)
                .withGenre(this.genre)
                .withLength(this.length)
                .build();

        return movieDto;
    }
}

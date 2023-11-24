package com.epam.training.ticketservice.core.room.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name;

    private int chairRows;

    private int chairColumns;

    public Room(String name, int chairRows, int chairColumns) {
        this.name = name;
        this.chairRows = chairRows;
        this.chairColumns = chairColumns;
    }
}

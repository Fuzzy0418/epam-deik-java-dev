package com.epam.training.ticketservice.core.room.model;

import lombok.Value;

@Value
public class RoomDto {

    private final String name;
    private final int chairRows;
    private final int chairColumns;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private int chairRows;
        private int chairColumns;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withChairRows(int chairRows) {
            this.chairRows = chairRows;
            return this;
        }

        public Builder withChairColumns(int chairColumns) {
            this.chairColumns = chairColumns;
            return this;
        }

        public RoomDto build() {
            return new RoomDto(name, chairRows, chairColumns);
        }
    }

    @Override
    public String toString() {
        return "Room " + name + " with " + (chairRows * chairColumns) + " seats, " + chairRows
                + " rows and " + chairColumns + " columns";
    }
}

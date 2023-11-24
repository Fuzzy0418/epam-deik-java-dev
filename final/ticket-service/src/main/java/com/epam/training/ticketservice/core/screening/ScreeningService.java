package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    List<ScreeningDto> listScreenings();

    Screening createScreening(String movieName, String roomName, LocalDateTime startTime);

    void deleteScreening(ScreeningDto screeningDto);

}

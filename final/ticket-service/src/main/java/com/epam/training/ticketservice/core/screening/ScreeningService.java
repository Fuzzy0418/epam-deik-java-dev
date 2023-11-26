package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    List<ScreeningDto> listScreenings();

    String createScreening(String movieName, String roomName, LocalDateTime startTime);

    String deleteScreening(String movieName, String roomName, LocalDateTime startTime);

}

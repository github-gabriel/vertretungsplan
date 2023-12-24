package de.gabriel.vertretungsplan.model.dto;

import de.gabriel.vertretungsplan.model.enums.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentDay {

    private Day currentDay;

    private LocalDateTime currentDateTime;

}

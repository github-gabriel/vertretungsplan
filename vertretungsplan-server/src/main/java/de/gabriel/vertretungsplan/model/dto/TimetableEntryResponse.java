package de.gabriel.vertretungsplan.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimetableEntryResponse {

    private int hour;

    private String course;

    private String subject;

    private String teacher;

    private String day;

}

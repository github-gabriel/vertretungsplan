package de.gabriel.vertretungsplan.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherResponse {

    private String name;

    private String initials;

    private String email;

    private String role;

    private String attendance;

    private List<String> subjects;

}

package de.gabriel.vertretungsplan.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdministrationResponse {

    private String name;

    private String email;

    private String role;

}

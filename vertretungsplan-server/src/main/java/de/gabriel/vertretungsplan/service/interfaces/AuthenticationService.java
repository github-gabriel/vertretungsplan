package de.gabriel.vertretungsplan.service.interfaces;

import de.gabriel.vertretungsplan.model.dto.AuthenticationRequest;

public interface AuthenticationService {

    String authenticate(AuthenticationRequest authenticationRequest);

}

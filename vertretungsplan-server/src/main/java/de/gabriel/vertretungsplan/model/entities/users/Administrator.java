package de.gabriel.vertretungsplan.model.entities.users;

import de.gabriel.vertretungsplan.model.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "administrator")
public class Administrator extends User {

    public Administrator(String name, String email, String password) {
        super(name, email, password, Role.getRoleWithPrefix(Role.ADMINISTRATOR));
    }

}

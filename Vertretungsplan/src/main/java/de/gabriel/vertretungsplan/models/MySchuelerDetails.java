package de.gabriel.vertretungsplan.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MySchuelerDetails implements UserDetails {

    private String userName;
    private String password;
    private String klasse;
    private boolean active;
    private List<GrantedAuthority> authorities;

    public MySchuelerDetails(Schueler schueler) {
        this.userName = schueler.getUserName();
        this.password = schueler.getPassword();
        this.klasse = schueler.getKlasse();
        this.active = schueler.isActive();
        this.authorities = Arrays.stream(schueler.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        System.out.println(userName);
        System.out.println(password);
        System.out.println(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
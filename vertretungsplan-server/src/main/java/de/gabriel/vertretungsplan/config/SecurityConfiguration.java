package de.gabriel.vertretungsplan.config;

import de.gabriel.vertretungsplan.model.enums.Role;
import de.gabriel.vertretungsplan.security.filter.ExceptionHandlerFilter;
import de.gabriel.vertretungsplan.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static de.gabriel.vertretungsplan.config.ApplicationConfig.*;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/v1/auth/**", "/api/v1/resources/day", "/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/api/v1/resources/substitution-plan").hasAnyRole(Role.getRoleAsString(Role.STUDENT), Role.getRoleAsString(Role.TEACHER), Role.getRoleAsString(Role.ADMINISTRATOR))
                        .requestMatchers("/api/v1/resources" + USER_ENDPOINT + "/**").hasAnyRole(Role.getRoleAsString(Role.STUDENT), Role.getRoleAsString(Role.TEACHER), Role.getRoleAsString(Role.ADMINISTRATOR))
                        .requestMatchers("/api/v1/resources " + TEACHER_ENDPOINT + "/**").hasRole(Role.getRoleAsString(Role.TEACHER))
                        .requestMatchers("/api/v1/resources" + ADMINISTRATOR_ENDPOINT + "/**").hasRole(Role.getRoleAsString(Role.ADMINISTRATOR))
                        .anyRequest().authenticated()
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Disable session creation
                .and()
                .userDetailsService(userDetailsService) // Allows adding an additional UserDetailsService to be used
                .authenticationProvider(authenticationProvider) // Allows adding an additional AuthenticationProvider to be used
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT authentication filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class); // Add exception handler filter before JwtAuthenticationFilter
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(CROSS_ORIGIN_URL)); // Allow requests from this origin
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "OPTIONS")); // Allow these HTTP methods
        configuration.setAllowedHeaders(List.of("Content-Type")); // Allow these headers
        configuration.setAllowCredentials(true); // Allow credentials to be included in CORS requests

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}

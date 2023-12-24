package de.gabriel.vertretungsplan.security.filter;

import de.gabriel.vertretungsplan.exception.exceptions.authentication.InvalidJwtException;
import de.gabriel.vertretungsplan.security.UserDetailsImpl;
import de.gabriel.vertretungsplan.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.gabriel.vertretungsplan.config.ApplicationConfig.API_BASE;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Cookie jwtCookie = WebUtils.getCookie(request, "jwt");

        final String jwtToken;

        if (jwtCookie == null) {
            log.debug("Skipping filter due to an empty JWT cookie; {[Filter={}]}",
                    this.getClass());
            filterChain.doFilter(request, response);
            throw new InvalidJwtException("The JWT Token is empty!");
        }

        jwtToken = jwtCookie.getValue();
        checkToken(request, response, filterChain, jwtToken);
    }

    private void checkToken(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain, String jwtToken) throws IOException, ServletException {
        String email;
        try {
            email = jwtService.extractUsername(jwtToken);
            log.debug("Email successfully extracted from JWT Token; {[Email={}], [Filter={}]}",
                    email, this.getClass());
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException("The JWT Token you provided is invalid!");
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) this.userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("User successfully authenticated; {[Email={}], [Filter={}]}",
                        email, this.getClass());
            } else {
                throw new InvalidJwtException("The JWT Token is invalid!");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        List<String> excludeUrlPatterns = new ArrayList<>();
        excludeUrlPatterns.add(API_BASE + "/auth/**");
        excludeUrlPatterns.add("/api-docs-ui/**");
        AntPathMatcher pathMatcher = new AntPathMatcher();

        return excludeUrlPatterns.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

}

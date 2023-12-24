package de.gabriel.vertretungsplan.utils.logging;

import de.gabriel.vertretungsplan.model.entities.users.User;
import de.gabriel.vertretungsplan.service.JwtService;
import de.gabriel.vertretungsplan.utils.logging.dto.LogDtoConversion;
import de.gabriel.vertretungsplan.utils.logging.request.LogRequest;
import de.gabriel.vertretungsplan.utils.logging.user.LogUserSave;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final PasswordEncoder passwordEncoder;

    @Around("@annotation(logRequest)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogRequest logRequest) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String message = logRequest.message();

        if (message.isEmpty()) {
            message = "Received Request";
        }

        JwtService jwtService = new JwtService();

        Object[] args = joinPoint.getArgs();

        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    String email = null;
                    HttpServletRequest request;

                    request = (HttpServletRequest) arg;

                    try {
                        Cookie[] cookie = request.getCookies();

                        for (Cookie c : cookie) {
                            if (c.getName().equals("jwt")) {
                                email = jwtService.extractUsername(c.getValue());
                            }
                        }
                    } catch (Exception ignored) {
                    }

                    log.info("{}; {[IP Address={}], [Method={}], [Endpoint={}], [Email={}], [Signature={}], [ExecutionTime={}ms]}",
                            message, request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), email == null ? "" : email, signature, (endTime - startTime));
                }
            }
        }
        return proceed;
    }

    @Around("@annotation(logDtoConversion)")
    public Object logDtoConversion(ProceedingJoinPoint joinPoint, LogDtoConversion logDtoConversion) throws Throwable {
        String message = logDtoConversion.message();
        Class<?> dtoClass = logDtoConversion.dto();
        Class<?> entityClass = logDtoConversion.entity();

        log.debug("{}; {[Source={}], [Destination={}]}", message, dtoClass.getSimpleName(), entityClass.getSimpleName());

        return joinPoint.proceed();
    }

    @Around("@annotation(logUserSave)")
    public Object logUserSave(ProceedingJoinPoint proceedingJoinPoint, LogUserSave logUserSave) throws Throwable {
        String message = logUserSave.message();

        Object[] args = proceedingJoinPoint.getArgs();

        List<String> emails = new ArrayList<>();

        for (Object arg : args) {
            if (arg instanceof List<?> list) {
                if (!list.isEmpty()) {
                    for (Object element : list) {
                        if (element instanceof User) {
                            emails.add(((User) element).getEmail());
                        }
                    }
                }
            } else if (arg instanceof User) {
                emails.add(((User) arg).getEmail());
            }
        }
        log.debug("{}; {[PasswordEncryption={}], [Users={}], [Signature={}]}", message, passwordEncoder.getClass().getSimpleName(), emails, proceedingJoinPoint.getSignature());

        return proceedingJoinPoint.proceed();
    }

}

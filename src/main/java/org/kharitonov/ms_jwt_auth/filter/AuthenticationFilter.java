package org.kharitonov.ms_jwt_auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kharitonov.ms_jwt_auth.exceptions.JwtNotValidException;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.security.JwtProvider;
import org.kharitonov.ms_jwt_auth.service.UserService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Order(1)
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    public AuthenticationFilter(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/v1/hello")) {
            try {
                String token = request.getHeader("token");

                String username = jwtProvider.extractUsername(token);
                User dbUser = userService.findByUsername(username);
                boolean isValid = jwtProvider.isTokenValid(token, dbUser);

                request.setAttribute("role", dbUser.getRoles());

                if (!dbUser.isActive() && !isValid) {
                    response.sendError(401, "unauthorized");
                    filterChain.doFilter(request, response);
                    throw new JwtNotValidException(
                            "Неправильная конфигурация подписи / невозможно декодировать Claims");
                }
            } catch (Exception e) {
                response.sendError(401, e.getMessage());
                filterChain.doFilter(request, response);
            }
        }
        filterChain.doFilter(request, response);
    }
}

package org.kharitonov.ms_jwt_auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms_jwt_auth.exceptions.JwtNotValidException;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.UserRole;
import org.kharitonov.ms_jwt_auth.security.JwtService;
import org.kharitonov.ms_jwt_auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/v1/test")) {
            try {
                String token = request.getHeader("token");

                String username = jwtService.extractUsername(token);
                UserRole role = jwtService.extractRole(token);
                User user = userService.findByUsername(username);
                boolean isValid = jwtService.isTokenValid(token, user);
            } catch (Exception e) {
                throw new JwtNotValidException("Invalid Signing configuration or JWT was expired",
                        HttpStatus.UNAUTHORIZED);
            }
        }
        filterChain.doFilter(request, response);

    /*@Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistrationBean() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtService, userService));
        registrationBean.setOrder(2);

        registrationBean.setName("jwtBearer");
        registrationBean.setUrlPatterns(Collections.singleton("/api/v1/test/*"));
        return registrationBean;
    }*/
    }
}

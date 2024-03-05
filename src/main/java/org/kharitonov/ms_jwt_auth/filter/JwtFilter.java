package org.kharitonov.ms_jwt_auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms_jwt_auth.exceptions.JwtNotValidException;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.security.JwtService;
import org.kharitonov.ms_jwt_auth.service.UserService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Slf4j
@Order(1)
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
                User dbUser = userService.findByUsername(username);
                boolean isValid = jwtService.isTokenValid(token, dbUser);

                request.setAttribute("role", Collections.singletonList(dbUser.getRoles()));

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

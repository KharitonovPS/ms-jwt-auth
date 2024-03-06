package org.kharitonov.ms_jwt_auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kharitonov.ms_jwt_auth.model.UserRole;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Kharitonov Pavel on 06.03.2024.
 */
@Component
@Order(2)
public class AuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/v1/hello/admin")) {
            try {
                Object token = request.getAttribute("role");
                HashSet roles = new HashSet<>((Collection) token);
                if (roles.contains(UserRole.ROLE_ADMIN)) {
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(403, "Forbidden");
                    filterChain.doFilter(request, response);
                }
            } catch (Exception e) {
                response.sendError(401, "Invalid JWT token");
                filterChain.doFilter(request, response);
            }
        }
        filterChain.doFilter(request, response);

    }
}

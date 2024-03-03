package org.kharitonov.ms_jwt_auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Component
@Order(1)
@Slf4j
public class SecurityFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info(Arrays.toString(httpRequest.getCookies()));
        chain.doFilter(httpRequest, response);
    }
}

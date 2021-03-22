package ai.leantech.restftp.security;

import ai.leantech.restftp.config.FtpProperties;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TokenAuthenticationFilter implements Filter {

    private static final String AUTHORIZATION_HEADER_KEY = "X-Authorization";

    private FtpProperties ftpProperties;

    public TokenAuthenticationFilter(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader(AUTHORIZATION_HEADER_KEY);

        boolean matches = Objects.equals(token, ftpProperties.getUser().getToken());
        if (matches) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}

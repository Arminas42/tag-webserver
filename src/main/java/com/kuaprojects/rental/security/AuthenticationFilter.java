package com.kuaprojects.rental.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationFilter extends GenericFilterBean {
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    @Autowired
    private ApiKeyService service;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String apiKey = ((HttpServletRequest) request).getHeader(AUTH_TOKEN_HEADER_NAME);
            //https://stackoverflow.com/questions/76084548/spring-boot-permitall-not-working-using-securityfilterchain
            if (apiKey == null) {
                chain.doFilter(request, response);
                return;
            }
            Authentication authentication = AuthenticationService.getAuthentication(apiKey, service);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception exp) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpResponse.getWriter();
            writer.print(exp.getMessage());
            writer.flush();
            writer.close();
        }

        chain.doFilter(request, response);
    }

}

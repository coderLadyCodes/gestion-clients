package com.samia.gestion.clients.security;

import com.samia.gestion.clients.entity.Jwt;
import com.samia.gestion.clients.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;

@Service
public class JwtFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final UserService userService;
    private final JwtService jwtService;

    public JwtFilter(HandlerExceptionResolver handlerExceptionResolver, UserService userService, JwtService jwtService) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.userService = userService;
        this.jwtService = jwtService;

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/login") || request.getServletPath().startsWith("/refresh-token");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        Jwt tokenInDb = null;
        String username = null;
        boolean isTokenExpired = true;

        try {
            String authorization = request.getHeader("Authorization");
            Cookie[] cookies = request.getCookies();
            if(authorization == null && cookies != null){
                authorization = getCookieValue(request, "token");
            }
          System.out.println("Cookie " + authorization);
            if (cookies != null && authorization != null && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);

                tokenInDb = this.jwtService.tokenByValue(token);
                isTokenExpired = jwtService.isTokenExpired(token);
                username = jwtService.extractUsername(token);
            }
            System.out.println("63 is token expired " + isTokenExpired);
            if (!isTokenExpired
                    && tokenInDb.getUser().getEmail().equals(username)

                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("68 is token expired " + isTokenExpired);
                UserDetails userDetails = userService.loadUserByUsername(username);
                if (userDetails != null) {
                    System.out.println("71 is token expired " + isTokenExpired);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, true, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
            response.getWriter().write(e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return;
        } catch (Exception exception) {

            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
        filterChain.doFilter(request, response);
    }
    private String getCookieValue(HttpServletRequest req, String cookieName) {
        String token =  Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        if(token == null) return null;
        return String.format("Bearer %s", token);
    }
}

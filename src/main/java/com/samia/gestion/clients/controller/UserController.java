package com.samia.gestion.clients.controller;

import com.samia.gestion.clients.DTO.AuthenticationDTO;
import com.samia.gestion.clients.DTO.UserDTO;
import com.samia.gestion.clients.entity.Jwt;
import com.samia.gestion.clients.entity.Role;
import com.samia.gestion.clients.entity.User;
import com.samia.gestion.clients.exception.NotFoundException;
import com.samia.gestion.clients.exception.UnauthorizedException;
import com.samia.gestion.clients.repository.JwtRepository;
import com.samia.gestion.clients.repository.UserRepository;
import com.samia.gestion.clients.security.JwtService;
import com.samia.gestion.clients.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService, JwtRepository jwtRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.jwtRepository = jwtRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/identification")
    public String identification() {
        return "identifier";
    }

    @PostMapping("/inscription")
    public void inscription(@RequestBody User user) throws Exception {
        if (!userRepository.existsByEmail(user.getEmail())) {
            user.setActif(true);
            user.setRole(Role.USER);
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else {
            System.out.println("User with email " + user.getEmail() + " already exists.");
        }
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthenticationDTO authDTO, HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        if (auth.isAuthenticated()) {
            Map<String, String> tokens = jwtService.generate(authDTO.username());
            User user = (User) auth.getPrincipal();
            tokens.put("role", user.getRole().name());
            tokens.put("id", String.valueOf(user.getId()));
            tokens.put("userName", user.getName());
            tokens.put("phone", user.getPhone());
            addTokenCookies(tokens, response);
            return tokens;
        }




        throw new UnauthorizedException("Authentication failed");
    }

    @PostMapping("/refresh-token")
    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.getCookieValue(request, "refresh");
        if (refreshToken == null) {
            throw new UnauthorizedException("Refresh token not found in cookies");
        }
        Map<String, String> tokens = jwtService.refreshToken(Map.of("refresh", refreshToken));
        addTokenCookies(tokens, response);
        return tokens;
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtService.getCookieValue(request, "token");
        if (token != null) {
            try {
                Jwt jwt = jwtService.tokenByValue(token);
                jwt.setExpire(true);
                jwt.setDesactive(true);
                jwtRepository.save(jwt);
            } catch (NotFoundException e) {
                // Log token not found but continue with logout
            }
        }
        clearTokenCookies(response);
        SecurityContextHolder.clearContext();
    }

    public void addTokenCookies(Map<String, String> tokens, HttpServletResponse response) {
        boolean isSecure = Boolean.parseBoolean(System.getenv().getOrDefault("COOKIE_SECURE", "true"));
        setCookie(response, "token", tokens.get("bearer"),  1800, isSecure);
        setCookie(response, "refresh", tokens.get("refresh"),3600 , isSecure);
    }

    public void clearTokenCookies(HttpServletResponse response) {
        setCookie(response, "token", null, 0, true);
        setCookie(response, "refresh", null, 0, true);
    }

    private void setCookie(HttpServletResponse response, String name, String value, int maxAge, boolean isSecure) {
        //Cookie cookie = new Cookie(name, value);

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(isSecure)
                .sameSite(isSecure ? "None" : "Lax")
                .path("/")
                .maxAge(Duration.ofSeconds(maxAge))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @GetMapping("/user/{id}")
    public UserDTO getUserById(@PathVariable(value = "id") Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/user/{id}")
    public UserDTO updateUser(@RequestBody UserDTO userDetails, @PathVariable(value = "id") Long id){
      return userService.updateUser(id, userDetails) ;
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

}

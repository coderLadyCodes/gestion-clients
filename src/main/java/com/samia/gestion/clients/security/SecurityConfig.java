package com.samia.gestion.clients.security;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService, JwtFilter jwtFilter){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return
                httpSecurity
                        .cors(Customizer.withDefaults())
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(
                                authorize ->
                                        authorize
                                                .requestMatchers(POST,"/login").permitAll()
                                                .requestMatchers(POST,"/logout").permitAll()
                                                .requestMatchers(POST,"/refresh-token").permitAll()
                                                .requestMatchers(GET,"/user/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(PUT,"/user/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/users").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(DELETE,"/user/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(POST,"/client").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/client/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(PUT,"/client/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/clients").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(DELETE,"/client/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(POST,"/category").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/category/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/category/{name}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(PUT,"/category/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/categories").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(DELETE,"/category/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(POST,"/product").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/product/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(PUT,"/product/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(DELETE,"/product/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/product/name/{name}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/products").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(POST,"/care").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/care/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(PUT,"/care/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/cares/{clientId}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(DELETE,"/care/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(POST,"/program").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/program/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(PUT,"/program/update/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(GET,"/programs/{clientId}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .requestMatchers(DELETE,"/program/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                                .anyRequest().authenticated()
                        )
                        .sessionManagement(httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .logout(AbstractHttpConfigurer::disable)
                        .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        //configuration.setAllowedHeaders(List.of("Authorization","Content-Type","X-XSRF-TOKEN", "X-Requested-With", "Accept"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));

        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Set-Cookie"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }
}

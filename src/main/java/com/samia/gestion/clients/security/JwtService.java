package com.samia.gestion.clients.security;

import com.samia.gestion.clients.entity.Jwt;
import com.samia.gestion.clients.entity.RefreshToken;
import com.samia.gestion.clients.entity.User;
import com.samia.gestion.clients.exception.NotFoundException;
import com.samia.gestion.clients.exception.UnauthorizedException;
import com.samia.gestion.clients.repository.JwtRepository;
import com.samia.gestion.clients.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@Configuration
@PropertySource("application.properties")
public class JwtService {
    public static final String BEARER = "bearer";
    public static final String REFRESH = "refresh";

    @Value("${encription.key}")
    private String ENCRIPTION_KEY;

    private final UserService userService;
    private final JwtRepository jwtRepository;

    public JwtService(UserService userService, JwtRepository jwtRepository) {
        this.userService = userService;
        this.jwtRepository = jwtRepository;
    }

    public Jwt tokenByValue(String value) {
        return this.jwtRepository.findByValeurAndDesactiveAndExpire(value, false, false).orElseThrow(()-> new NotFoundException("Token inconnu" + value));
    }

    public Map<String, String> generate(String username){
        User user = this.userService.loadUserByUsername(username);
        this.disableTokens(user);
        final Map<String, String> jwtMap = new java.util.HashMap<>(this.generateJwt(user));
        RefreshToken refreshToken = RefreshToken
                .builder()
                .valeur(UUID.randomUUID().toString())
                .expire(false)
                .creation(Instant.now())
                .expiration(Instant.now().plusSeconds( 3600))
                .build();
        final Jwt jwt = Jwt
                .builder()
                .valeur(jwtMap
                .get(BEARER))
                .desactive(false)
                .expire(false)
                .user(user)
                .refreshToken(refreshToken)
                .build();
        this.jwtRepository.save(jwt);
        jwtMap.put(REFRESH, refreshToken.getValeur());
        return jwtMap;
    }

    private void disableTokens(User user) {
        final List<Jwt> jwtList = this.jwtRepository.findByEmail(user.getEmail()).peek(
                jwt -> {
                    jwt.setDesactive(true);
                    jwt.setExpire(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtList);
    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

public boolean isTokenExpired(String token) { //, Jwt tokenInDb
    Date expirationDate = getExpirationDateFromToken(token);
    return expirationDate.before(new Date());
}

    private Date getExpirationDateFromToken(String token){
        return this.getClaim(token, Claims::getExpiration);
    }
    private <T> T getClaim(String token, Function<Claims,T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Map<String, String> generateJwt(User user) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 5 * 60 * 1000;

        final Map<String, Object> claims =  Map.of(
                "name", user.getName(),
                Claims.EXPIRATION,new Date(expirationTime),
                Claims.SUBJECT,user.getEmail()
        );
        final String bearer = Jwts.builder()
                .issuer("self")
                .issuedAt(new Date(currentTime))
                .expiration(new Date(expirationTime))
                .subject(user.getEmail())
                .claims(claims)
                .signWith(getKey())
                .compact();
        return Map.of(BEARER, bearer);
    }
    private SecretKey getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public Map<String, String> refreshToken(Map<String, String> refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.get(REFRESH);
        final Optional<Jwt> ojwt = this.jwtRepository.findByRefreshToken(refreshToken);
        Jwt jwt = ojwt.get();
        if(jwt.getRefreshToken().isExpire() || jwt.getRefreshToken().getExpiration().isBefore(Instant.now())){
            jwt.getRefreshToken().setExpire(true);
            jwtRepository.save(jwt);
            throw new UnauthorizedException("token invalide");
        }
        this.disableTokens(jwt.getUser());
        return this.generate(jwt.getUser().getEmail());
    }
    public String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    @Scheduled(cron = "@daily")
    public void removeUselessJwt(){
        this.jwtRepository.deleteAllByExpireAndDesactive(true, true);
        System.out.println("Expired tokens cleaned up.");
    }
}

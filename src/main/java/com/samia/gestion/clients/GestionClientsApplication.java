package com.samia.gestion.clients;

import com.samia.gestion.clients.entity.*;
import com.samia.gestion.clients.repository.CategoryRepository;
import com.samia.gestion.clients.repository.ProductRepository;
import com.samia.gestion.clients.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties
@EnableScheduling
@SpringBootApplication
public class GestionClientsApplication  implements CommandLineRunner {

	@Value("${user.password}")
	private String userPassword;

	@Value("${user.email}")
	private String userEmail;

	@Value("${user.phone}")
	private String userPhone;

	@Value("${user.fullname}")
	private String userName;

	@Value("${user.role}")
	private Role userRole;

	@Value("${user.actif}")
	private boolean userActif;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	public GestionClientsApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
    }

	public static void main(String[] args) {
		SpringApplication.run(GestionClientsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!userRepository.existsByEmail(userEmail)) {
			User user = User.builder()
					.actif(userActif)
					.name(userName)
					.email(userEmail)
					.phone(userPhone)
					.password(passwordEncoder.encode(userPassword))
					.role(userRole)
					.build();
			userRepository.save(user);
		} else {
			System.out.println("User with email " + userEmail + " already exists.");
		}
	}
}
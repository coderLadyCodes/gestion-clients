package com.samia.gestion.clients.service;

import com.samia.gestion.clients.DTO.UserDTO;
import com.samia.gestion.clients.entity.User;
import com.samia.gestion.clients.exception.NotFoundException;
import com.samia.gestion.clients.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;



    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User mapToUser(UserDTO userDTO){
        return new User(
                userDTO.id(),
                userDTO.name(),
                userDTO.email(),
                userDTO.phone(),
                userDTO.password(),
                userDTO.role()
        );
    }

    public UserDTO mapToUserDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getRole()
        );
    }

    @Override
    public User loadUserByUsername(String username) throws NotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new NotFoundException("Aucun utilisateur dans la base de donnée"));
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    public UserDTO getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));
        return mapToUserDTO(user);
    }

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO userDetails){
        User originalUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        originalUser.setName(userDetails.name());
        originalUser.setEmail(userDetails.email());
        originalUser.setPhone(userDetails.phone());
        User updatedUser = userRepository.save(originalUser);
        return mapToUserDTO(updatedUser);
    }
}

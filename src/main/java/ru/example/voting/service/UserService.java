package ru.example.voting.service;

import org.springframework.stereotype.Service;
import ru.example.voting.model.User;
import ru.example.voting.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserNameByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        return userOpt.map(User::getName).orElse("Unknown User");
    }

}

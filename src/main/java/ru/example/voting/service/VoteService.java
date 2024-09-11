package ru.example.voting.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.example.voting.model.Restaurant;
import ru.example.voting.model.User;
import ru.example.voting.model.Vote;
import ru.example.voting.repository.RestaurantRepository;
import ru.example.voting.repository.UserRepository;
import ru.example.voting.repository.VoteRepository;

import java.time.LocalDate;

@Service
public class VoteService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public VoteService(RestaurantRepository restaurantRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(Integer restaurantId) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            throw new IllegalArgumentException("User not found");
        }
        String email = userDetails.getUsername();

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant with id " + restaurantId + " not found"));

        LocalDate today = LocalDate.now();
        boolean alreadyVoted = voteRepository.existsByUserIdAndVoteDate(user.getId(), today);
        if (alreadyVoted) {
            throw new IllegalArgumentException(user.getName() + " has already voted today");
        }

        Vote vote = new Vote(user, restaurant, today);
        voteRepository.save(vote);
    }

}

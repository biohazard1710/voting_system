package ru.example.voting.service;

import org.springframework.stereotype.Service;
import ru.example.voting.model.Restaurant;
import ru.example.voting.model.User;
import ru.example.voting.model.Vote;
import ru.example.voting.repository.RestaurantRepository;
import ru.example.voting.repository.UserRepository;
import ru.example.voting.repository.VoteRepository;
import ru.example.voting.to.VoteTo;

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

    public VoteResult vote(VoteTo voteTo) {

        User user = userRepository.findById(voteTo.getUserId()).orElse(null);
        if (user == null) {
            return VoteResult.USER_NOT_FOUND;
        }

        Restaurant restaurant = restaurantRepository.findById(voteTo.getRestaurantId()).orElse(null);
        if (restaurant == null) {
            return VoteResult.RESTAURANT_NOT_FOUND;
        }

        LocalDate today = LocalDate.now();
        boolean alreadyVoted = voteRepository.existsByUserIdAndVoteDate(user, today);
        if (alreadyVoted) {
            return VoteResult.ALREADY_VOTED;
        }

        Vote vote = new Vote(user, restaurant, today);
        voteRepository.save(vote);
        return VoteResult.SUCCESS;
    }

}

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
import ru.example.voting.to.VoteInputTo;

import java.time.LocalDate;
import java.util.Optional;

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

    public VoteResult vote(VoteInputTo voteInputTo) {

        // Получаем текущего аутентифицированного пользователя
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return VoteResult.USER_NOT_FOUND;
        }
        UserDetails userDetails = (UserDetails) principal;
        String email = userDetails.getUsername();

        // Находим пользователя по email
        Optional<User> optUser = userRepository.findByEmailIgnoreCase(email);
        if (optUser.isEmpty()) {
            return VoteResult.USER_NOT_FOUND;
        }

        // Проверяем наличие ресторана и, если все ок, сохраняем голос
        Optional<Restaurant> optRestaurant = restaurantRepository.findById(voteInputTo.getRestaurantId());
        if (optRestaurant.isEmpty()) {
            return VoteResult.RESTAURANT_NOT_FOUND;
        }

        LocalDate today = LocalDate.now();
        boolean alreadyVoted = voteRepository.existsByUserIdAndVoteDate(optUser.get().getId(), today);
        if (alreadyVoted) {
            return VoteResult.ALREADY_VOTED;
        }

        User user = optUser.get();
        Restaurant restaurant = optRestaurant.get();

        Vote vote = new Vote(user, restaurant, today);
        voteRepository.save(vote);
        return VoteResult.SUCCESS;
    }

}

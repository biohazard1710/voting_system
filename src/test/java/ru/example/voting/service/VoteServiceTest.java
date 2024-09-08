package ru.example.voting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.example.voting.model.Restaurant;
import ru.example.voting.model.Role;
import ru.example.voting.model.User;
import ru.example.voting.model.Vote;
import ru.example.voting.repository.RestaurantRepository;
import ru.example.voting.repository.UserRepository;
import ru.example.voting.repository.VoteRepository;
import ru.example.voting.to.VoteTo;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VoteServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private VoteService voteService;

    private LocalDate today;
    private User user;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        today = LocalDate.now();
        user = new User(1, "user", "user@mail.ru", "password", Role.USER);
        restaurant = new Restaurant(1, "Restaurant №1");
    }

    @Test
    void testSuccessVote() {
        VoteTo voteTo = new VoteTo(1, 1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(voteRepository.existsByUserIdAndVoteDate(user, today)).thenReturn(false);

        VoteResult result = voteService.vote(voteTo);

        assertThat(result).isEqualTo(VoteResult.SUCCESS);
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    void testUserNotFound() {
        VoteTo voteTo = new VoteTo(1, 1);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        VoteResult result = voteService.vote(voteTo);

        assertThat(result).isEqualTo(VoteResult.USER_NOT_FOUND);
        verify(voteRepository, never()).save(any(Vote.class));
    }

    @Test
    void testRestaurantNotFound() {
        VoteTo voteTo = new VoteTo(1, 1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(1)).thenReturn(Optional.empty());

        VoteResult result = voteService.vote(voteTo);

        assertThat(result).isEqualTo(VoteResult.RESTAURANT_NOT_FOUND);
        verify(voteRepository, never()).save(any(Vote.class));
    }

    @Test
    void testAlreadyVotedToday() {
        VoteTo voteTo = new VoteTo(1, 1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(voteRepository.existsByUserIdAndVoteDate(user, today)).thenReturn(true);

        VoteResult result = voteService.vote(voteTo);

        assertThat(result).isEqualTo(VoteResult.ALREADY_VOTED);
        verify(voteRepository, never()).save(any(Vote.class));
    }
}

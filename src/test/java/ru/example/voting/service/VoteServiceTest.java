package ru.example.voting.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import ru.example.voting.model.*;
import ru.example.voting.repository.RestaurantRepository;
import ru.example.voting.repository.UserRepository;
import ru.example.voting.repository.VoteRepository;
import ru.example.voting.to.VoteInputTo;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    private VoteInputTo voteInputTo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        today = LocalDate.now();
        user = new User(1, "User", "user@example.com", "password123", Role.USER);
        restaurant = new Restaurant(1, "Restaurant №1");
        voteInputTo = new VoteInputTo(1, 1);
    }

    @Test
    void testSuccessVote() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(voteRepository.existsByUserIdAndVoteDate(1, today)).thenReturn(false);

        VoteResult result = voteService.vote(voteInputTo);

        verify(userRepository, times(1)).findById(1);
        verify(restaurantRepository, times(1)).findById(1);
        verify(voteRepository, times(1)).existsByUserIdAndVoteDate(1, today);
        verify(voteRepository, times(1)).save(any(Vote.class));

        assertThat(result).isEqualTo(VoteResult.SUCCESS);
    }

    @Test
    void testUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        VoteResult result = voteService.vote(voteInputTo);

        verify(userRepository, times(1)).findById(1);
        verify(restaurantRepository, never()).findById(anyInt());
        verify(voteRepository, never()).existsByUserIdAndVoteDate(anyInt(), any(LocalDate.class));
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(result).isEqualTo(VoteResult.USER_NOT_FOUND);
    }

    @Test
    void testRestaurantNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(1)).thenReturn(Optional.empty());

        VoteResult result = voteService.vote(voteInputTo);

        verify(userRepository, times(1)).findById(1);
        verify(restaurantRepository, times(1)).findById(1);
        verify(voteRepository, never()).existsByUserIdAndVoteDate(anyInt(), any(LocalDate.class));
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(result).isEqualTo(VoteResult.RESTAURANT_NOT_FOUND);
    }

    @Test
    void testAlreadyVotedToday() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(voteRepository.existsByUserIdAndVoteDate(1, today)).thenReturn(true);

        VoteResult result = voteService.vote(voteInputTo);

        verify(userRepository, times(1)).findById(1);
        verify(restaurantRepository, times(1)).findById(1);
        verify(voteRepository, times(1)).existsByUserIdAndVoteDate(1, today);
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(result).isEqualTo(VoteResult.ALREADY_VOTED);
    }

}

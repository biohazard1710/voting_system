package ru.example.voting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.example.voting.model.Restaurant;
import ru.example.voting.model.Role;
import ru.example.voting.model.User;
import ru.example.voting.model.Vote;
import ru.example.voting.repository.RestaurantRepository;
import ru.example.voting.repository.UserRepository;
import ru.example.voting.repository.VoteRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private Integer restaurantId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        today = LocalDate.now();
        user = new User(1, "User", "user@example.com", "password123", Role.USER);
        restaurant = new Restaurant(1, "Restaurant №1");
        restaurantId = 1;

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testSuccessVote() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(voteRepository.existsByUserIdAndVoteDate(user.getId(), today)).thenReturn(false);

        voteService.vote(restaurantId);

        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(voteRepository, times(1)).existsByUserIdAndVoteDate(user.getId(), today);
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    void testUserNotFound() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> voteService.vote(restaurantId));

        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(restaurantRepository, never()).findById(anyInt());
        verify(voteRepository, never()).existsByUserIdAndVoteDate(anyInt(), any(LocalDate.class));
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    void testRestaurantNotFound() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> voteService.vote(restaurantId));

        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(voteRepository, never()).existsByUserIdAndVoteDate(anyInt(), any(LocalDate.class));
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(exception.getMessage()).isEqualTo("Restaurant with id " + restaurantId + " not found");
    }

    @Test
    void testAlreadyVotedToday() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(voteRepository.existsByUserIdAndVoteDate(user.getId(), today)).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> voteService.vote(restaurantId));

        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(voteRepository, times(1)).existsByUserIdAndVoteDate(user.getId(), today);
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(exception.getMessage()).isEqualTo(user.getName() + " has already voted today");
    }
}

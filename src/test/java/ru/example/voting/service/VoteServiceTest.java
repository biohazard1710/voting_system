package ru.example.voting.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        voteInputTo = new VoteInputTo(1);

        // Mocking the SecurityContext
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
        when(restaurantRepository.findById(voteInputTo.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(voteRepository.existsByUserIdAndVoteDate(user.getId(), today)).thenReturn(false);

        VoteResult result = voteService.vote(voteInputTo);

        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(restaurantRepository, times(1)).findById(voteInputTo.getRestaurantId());
        verify(voteRepository, times(1)).existsByUserIdAndVoteDate(user.getId(), today);
        verify(voteRepository, times(1)).save(any(Vote.class));

        assertThat(result).isEqualTo(VoteResult.SUCCESS);
    }

    @Test
    void testUserNotFound() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.empty());

        VoteResult result = voteService.vote(voteInputTo);

        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(restaurantRepository, never()).findById(anyInt());
        verify(voteRepository, never()).existsByUserIdAndVoteDate(anyInt(), any(LocalDate.class));
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(result).isEqualTo(VoteResult.USER_NOT_FOUND);
    }

    @Test
    void testRestaurantNotFound() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(voteInputTo.getRestaurantId())).thenReturn(Optional.empty());

        VoteResult result = voteService.vote(voteInputTo);

        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(restaurantRepository, times(1)).findById(voteInputTo.getRestaurantId());
        verify(voteRepository, never()).existsByUserIdAndVoteDate(anyInt(), any(LocalDate.class));
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(result).isEqualTo(VoteResult.RESTAURANT_NOT_FOUND);
    }

    @Test
    void testAlreadyVotedToday() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(voteInputTo.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(voteRepository.existsByUserIdAndVoteDate(user.getId(), today)).thenReturn(true);

        VoteResult result = voteService.vote(voteInputTo);

        verify(userRepository, times(1)).findByEmailIgnoreCase(user.getEmail());
        verify(restaurantRepository, times(1)).findById(voteInputTo.getRestaurantId());
        verify(voteRepository, times(1)).existsByUserIdAndVoteDate(user.getId(), today);
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(result).isEqualTo(VoteResult.ALREADY_VOTED);
    }

}

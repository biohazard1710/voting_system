package ru.example.voting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.example.voting.model.*;
import ru.example.voting.repository.MenuRepository;
import ru.example.voting.repository.UserRepository;
import ru.example.voting.repository.VoteRepository;
import ru.example.voting.to.VoteOutputTo;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VoteServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private VoteService voteService;

    private LocalDate today;
    private User user;
    private Menu menu;
    private Integer menuId;
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        today = LocalDate.now();
        user = new User(1, "User", "user@example.com", "password123", Role.USER);
        Restaurant restaurant = new Restaurant(1, "Restaurant");
        menu = new Menu(1, restaurant, today, "Dish - 1.12, Dish - 2.30");
        menuId = menu.getId();

        principal = () -> user.getEmail();
    }

    @Test
    void testSuccessVote() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(voteRepository.existsByUserIdAndVoteDate(user.getId(), today)).thenReturn(false);

        Vote savedVote = new Vote(1, user, menu, today);
        when(voteRepository.save(any(Vote.class))).thenReturn(savedVote);

        Integer votedMenuId = voteService.vote(menuId, principal);

        verify(userRepository).findByEmailIgnoreCase(user.getEmail());
        verify(menuRepository).findById(menuId);
        verify(voteRepository).existsByUserIdAndVoteDate(user.getId(), today);
        verify(voteRepository).save(any(Vote.class));
        assertThat(votedMenuId).isEqualTo(menuId);
    }

    @Test
    void testUserNotFound() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> voteService.vote(menuId, principal));

        verify(userRepository).findByEmailIgnoreCase(user.getEmail());
        verifyNoInteractions(menuRepository);
        verifyNoInteractions(voteRepository);

        assertThat(exception.getMessage()).isEqualTo(VoteService.USER_NOT_FOUND_MESSAGE);
    }

    @Test
    void testMenuNotFound() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> voteService.vote(menuId, principal));

        verify(userRepository).findByEmailIgnoreCase(user.getEmail());
        verify(menuRepository).findById(menuId);
        verifyNoInteractions(voteRepository);

        String message = String.format(VoteService.MENU_NOT_FOUND_MESSAGE, menuId);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void testAlreadyVotedToday() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(voteRepository.existsByUserIdAndVoteDate(user.getId(), today)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> voteService.vote(menuId, principal));

        verify(userRepository).findByEmailIgnoreCase(user.getEmail());
        verify(menuRepository).findById(menuId);
        verify(voteRepository).existsByUserIdAndVoteDate(user.getId(), today);
        verify(voteRepository, never()).save(any(Vote.class));

        assertThat(exception.getMessage()).isEqualTo(VoteService.ALREADY_VOTED_TODAY_MESSAGE);
    }

    @Test
    void testGetTodayUserVote() {
        Vote vote = new Vote(1, user, menu, today);
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(voteRepository.findByUserIdAndVoteDate(user.getId(), today)).thenReturn(Optional.of(vote));

        VoteOutputTo voteOutput = voteService.getTodayUserVote(principal);

        verify(userRepository).findByEmailIgnoreCase(user.getEmail());
        verify(voteRepository).findByUserIdAndVoteDate(user.getId(), today);

        assertThat(voteOutput.getMenuId()).isEqualTo(menu.getId());
        assertThat(voteOutput.getRestaurantName()).isEqualTo(menu.getRestaurant().getName());
        assertThat(voteOutput.getDishes()).isEqualTo(menu.getDishes());
        assertThat(voteOutput.getVoteDate()).isEqualTo(today);
    }

    @Test
    void testGetTodayUserVoteUserNotFound() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> voteService.getTodayUserVote(principal));

        verify(userRepository).findByEmailIgnoreCase(user.getEmail());
        verifyNoInteractions(voteRepository);

        assertThat(exception.getMessage()).isEqualTo(VoteService.USER_NOT_FOUND_MESSAGE);
    }

    @Test
    void testGetTodayUserVoteNotFound() {
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(voteRepository.findByUserIdAndVoteDate(user.getId(), today)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> voteService.getTodayUserVote(principal));

        verify(userRepository).findByEmailIgnoreCase(user.getEmail());
        verify(voteRepository).findByUserIdAndVoteDate(user.getId(), today);

        assertThat(exception.getMessage()).isEqualTo(VoteService.NOT_VOTED_TODAY_MESSAGE);
    }

}

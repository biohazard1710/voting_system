package ru.example.voting.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.voting.model.Menu;
import ru.example.voting.model.User;
import ru.example.voting.model.Vote;
import ru.example.voting.repository.MenuRepository;
import ru.example.voting.repository.UserRepository;
import ru.example.voting.repository.VoteRepository;
import ru.example.voting.to.VoteOutputTo;

import java.security.Principal;
import java.time.LocalDate;

@Service
@Slf4j
public class VoteService {

    protected static final String USER_NOT_FOUND_MESSAGE = "User not found";
    protected static final String MENU_NOT_FOUND_MESSAGE = "Menu with id %d not found";
    protected static final String ALREADY_VOTED_TODAY_MESSAGE = "You have already voted today";
    protected static final String NOT_VOTED_TODAY_MESSAGE = "You didn't vote today";

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public VoteService(MenuRepository menuRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public Integer vote(Integer menuId, Principal principal) {
        User user = getUser(principal);
        Menu menu = getMenu(menuId);
        checkAlreadyVoted(user);
        Integer voteId = saveVote(user, menu);

        log.info("User with ID {} voted for menu with ID {}. Vote ID: {}", user.getId(), menu.getId(), voteId);

        return menu.getId();
    }

    public VoteOutputTo getTodayUserVote(Principal principal) {
        User user = getUser(principal);
        Vote vote = getTodayVote(user.getId());

        return new VoteOutputTo(
                vote.getMenu().getId(),
                vote.getMenu().getRestaurant().getName(),
                vote.getMenu().getDishes(),
                vote.getVoteDate()
        );
    }

    private User getUser(Principal principal) {
        String email = principal.getName();
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    log.error("User with email {} not found", email);
                    return new IllegalArgumentException(USER_NOT_FOUND_MESSAGE);
                });
    }

    private Menu getMenu(Integer menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> {
                    log.error("Menu with ID {} not found", menuId);
                    String message = String.format(MENU_NOT_FOUND_MESSAGE, menuId);
                    return new IllegalArgumentException(message);
                });
    }

    private void checkAlreadyVoted(User user) {
        LocalDate today = LocalDate.now();
        boolean alreadyVoted = voteRepository.existsByUserIdAndVoteDate(user.getId(), today);
        if (alreadyVoted) {
            log.warn("User with ID {} already voted today", user.getId());
            throw new IllegalArgumentException(ALREADY_VOTED_TODAY_MESSAGE);
        }
    }

    private Integer saveVote(User user, Menu menu) {
        Vote vote = new Vote(user, menu, LocalDate.now());
        Vote savedVote = voteRepository.save(vote);
        log.info("Saved vote with ID {}", savedVote.getId());
        return savedVote.getId();
    }

    private Vote getTodayVote(Integer userId) {
        LocalDate today = LocalDate.now();
        return voteRepository.findByUserIdAndVoteDate(userId, today)
                .orElseThrow(() -> {
                    log.error("Vote not found for user with ID {} on date {}", userId, today);
                    return new IllegalArgumentException(NOT_VOTED_TODAY_MESSAGE);
                });
    }

}

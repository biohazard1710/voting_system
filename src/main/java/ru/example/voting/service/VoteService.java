package ru.example.voting.service;

import org.springframework.stereotype.Service;
import ru.example.voting.model.Menu;
import ru.example.voting.model.User;
import ru.example.voting.model.Vote;
import ru.example.voting.repository.MenuRepository;
import ru.example.voting.repository.UserRepository;
import ru.example.voting.repository.VoteRepository;

import java.security.Principal;
import java.time.LocalDate;

@Service
public class VoteService {

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
        saveVote(user, menu);

        return menu.getId();
    }

    private User getUser(Principal principal) {
        String email = principal.getName();
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Menu getMenu(Integer menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu with id " + menuId + " not found"));
    }

    private void checkAlreadyVoted(User user) {
        LocalDate today = LocalDate.now();
        boolean alreadyVoted = voteRepository.existsByUserIdAndVoteDate(user.getId(), today);
        if (alreadyVoted) {
            throw new IllegalArgumentException("You have already voted today");
        }
    }

    private void saveVote(User user, Menu menu) {
        Vote vote = new Vote(user, menu, LocalDate.now());
        voteRepository.save(vote);
    }

}

package ru.example.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.example.voting.service.VoteService;
import ru.example.voting.util.exception.AdminNotAllowedException;

import java.security.Principal;

@RestController
@RequestMapping("api/votes")
@Tag(name = "Vote Controller", description = "Controller for managing votes")
public class VoteController {

    private static final String SUCCESS_VOTING = "You have successfully voted for a menu with id %d";
    private static final String ADMINS_NOT_VOTING = "Admins don't participate in voting";


    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    @Operation(summary = "Vote for a menu", description = "Allows a user to vote a specific menu")
    public ResponseEntity<String> vote(@RequestParam Integer menuId, Principal principal, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new AdminNotAllowedException(ADMINS_NOT_VOTING);
        }
        Integer votedMenuId = voteService.vote(menuId, principal);
        String message = String.format(SUCCESS_VOTING, votedMenuId);
        return ResponseEntity.ok(message);
    }
}

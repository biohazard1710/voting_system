package ru.example.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.example.voting.service.VoteService;
import ru.example.voting.to.VoteOutputTo;

import java.security.Principal;

@RestController
@RequestMapping("api/votes")
@PreAuthorize("hasAuthority('USER')")
@Tag(name = "Vote Controller", description = "Controller for managing votes")
public class VoteController {

    private static final String SUCCESS_VOTING = "You have successfully voted for a menu with id %d";

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/today")
    @Operation(summary = "Get today user vote", description = "Allows the user to get their own vote for today")
    public ResponseEntity<VoteOutputTo> getTodayUserVote(Principal principal) {
        VoteOutputTo voteOutputTo = voteService.getTodayUserVote(principal);
        return ResponseEntity.ok(voteOutputTo);
    }

    @PostMapping
    @Operation(summary = "Vote for a menu", description = "Allows a user to vote a specific menu")
    public ResponseEntity<String> vote(@RequestParam Integer menuId, Principal principal) {
        Integer votedMenuId = voteService.vote(menuId, principal);
        String message = String.format(SUCCESS_VOTING, votedMenuId);
        return ResponseEntity.ok(message);
    }

}


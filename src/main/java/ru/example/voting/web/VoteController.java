package ru.example.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.voting.service.UserService;
import ru.example.voting.service.VoteService;

import java.security.Principal;

@RestController
@RequestMapping("api/votes")
@Tag(name = "Vote Controller", description = "Controller for managing votes")
public class VoteController {

    private static final String SUCCESS_VOTING = "%s successfully voted for restaurant with id %d";

    private final VoteService voteService;
    private final UserService userService;

    public VoteController(VoteService voteService, UserService userService) {
        this.voteService = voteService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Vote for a restaurant", description = "Allows a user to vote a specific restaurant")
    public ResponseEntity<String> vote(@RequestParam Integer restaurantId, Principal principal) {
        String email = principal.getName();
        String userName = userService.getUserNameByEmail(email);

        voteService.vote(restaurantId);
        String successMessage = String.format(SUCCESS_VOTING, userName, restaurantId);
        return ResponseEntity.ok(successMessage);
    }
}

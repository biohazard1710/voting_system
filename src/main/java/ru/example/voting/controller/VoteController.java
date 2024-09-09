package ru.example.voting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.voting.service.VoteResult;
import ru.example.voting.service.VoteService;
import ru.example.voting.to.ErrorResponseOutputTo;
import ru.example.voting.to.VoteInputTo;

@RestController
@RequestMapping("api/votes")
@Tag(name = "Vote Controller", description = "Controller for managing votes")
public class VoteController {

    private static final String SUCCESS_VOTING = "User with id %d successfully vote for restaurant with id %d";
    private static final String USER_NOT_FOUND = "User with id %d not found";
    private static final String RESTAURANT_NOT_FOUND = "Restaurant with id %d not found";
    private static final String ALREADY_VOTING_TODAY = "User with id %d has already voted today";

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    @Operation(summary = "Vote for a restaurant", description = "Allows a user to vote a specific restaurant")
    public ResponseEntity<?> vote(@Valid @RequestBody VoteInputTo voteInputTo) {
        VoteResult result = voteService.vote(voteInputTo);
        Integer userId = voteInputTo.getUserId();
        Integer restaurantId = voteInputTo.getRestaurantId();

        switch (result) {
            case SUCCESS:
                String successVoting = String.format(SUCCESS_VOTING, userId, restaurantId);
                return ResponseEntity.ok(successVoting);
            case USER_NOT_FOUND:
                String userNotFound = String.format(USER_NOT_FOUND, userId);
                return ResponseEntity.badRequest().body(new ErrorResponseOutputTo("400", userNotFound));
            case RESTAURANT_NOT_FOUND:
                String restaurantNotFound = String.format(RESTAURANT_NOT_FOUND, restaurantId);
                return ResponseEntity.badRequest().body(new ErrorResponseOutputTo("400", restaurantNotFound));
            case ALREADY_VOTED:
                String alreadyVoted = String.format(ALREADY_VOTING_TODAY, userId);
                return ResponseEntity.badRequest().body(new ErrorResponseOutputTo("400", alreadyVoted));
            default:
                String unknownError = "Unknown error";
                return ResponseEntity.status(500).body(new ErrorResponseOutputTo("500", unknownError));
        }
    }
}

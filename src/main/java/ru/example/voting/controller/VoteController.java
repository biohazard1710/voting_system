package ru.example.voting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.voting.service.VoteResult;
import ru.example.voting.service.VoteService;
import ru.example.voting.to.VoteTo;

@RestController
@RequestMapping("api/votes")
@Tag(name = "Vote Controller", description = "Controller for managing votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    @Operation(summary = "Vote for a restaurant", description = "Allows a user to vote a specific restaurant")
    public ResponseEntity<String> vote(@RequestBody VoteTo voteTo) {
        VoteResult result = voteService.vote(voteTo);
        Integer userId = voteTo.getUserId();
        Integer restaurantId = voteTo.getRestaurantId();

        switch (result) {
            case SUCCESS:
                return ResponseEntity.ok("User with id " + userId + " successfully voted for restaurant with id " + restaurantId);
            case USER_NOT_FOUND:
                return ResponseEntity.badRequest().body("User with id " + userId + " not found");
            case RESTAURANT_NOT_FOUND:
                return ResponseEntity.badRequest().body("Restaurant with id " + restaurantId + " not found");
            case ALREADY_VOTED:
                return ResponseEntity.badRequest().body("User with id " + userId + " has already voted today");
            default:
                return ResponseEntity.status(500).body("Unknown error");
        }
    }
}

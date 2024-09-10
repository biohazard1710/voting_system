package ru.example.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.voting.service.UserService;
import ru.example.voting.service.VoteResult;
import ru.example.voting.service.VoteService;
import ru.example.voting.to.ErrorResponseOutputTo;
import ru.example.voting.to.VoteInputTo;

@RestController
@RequestMapping("api/votes")
@Tag(name = "Vote Controller", description = "Controller for managing votes")
public class VoteController {

    private static final String SUCCESS_VOTING = "%s successfully vote for restaurant with id %d";
    private static final String USER_NOT_FOUND = "%s not found";
    private static final String RESTAURANT_NOT_FOUND = "Restaurant with id %d not found";
    private static final String ALREADY_VOTING_TODAY = "%s has already voted today";

    private final VoteService voteService;
    private final UserService userService;

    public VoteController(VoteService voteService, UserService userService) {
        this.voteService = voteService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Vote for a restaurant", description = "Allows a user to vote a specific restaurant")
    public ResponseEntity<?> vote(@Valid @RequestBody VoteInputTo voteInputTo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        String userName = userService.getUserNameByEmail(email);

        VoteResult result = voteService.vote(voteInputTo);
        Integer restaurantId = voteInputTo.getRestaurantId();

        return switch (result) {
            case SUCCESS -> {
                String successVoting = String.format(SUCCESS_VOTING, userName, restaurantId);
                yield ResponseEntity.ok(successVoting);
            }
            case USER_NOT_FOUND -> {
                String notFound = String.format(USER_NOT_FOUND, userName);
                yield ResponseEntity.badRequest().body(new ErrorResponseOutputTo("400", notFound));
            }
            case RESTAURANT_NOT_FOUND -> {
                String restaurantNotFound = String.format(RESTAURANT_NOT_FOUND, restaurantId);
                yield ResponseEntity.badRequest().body(new ErrorResponseOutputTo("400", restaurantNotFound));
            }
            case ALREADY_VOTED -> {
                String alreadyVoted = String.format(ALREADY_VOTING_TODAY, userName);
                yield ResponseEntity.badRequest().body(new ErrorResponseOutputTo("400", alreadyVoted));
            }
            default -> {
                String unknownError = "Unknown error";
                yield ResponseEntity.status(500).body(new ErrorResponseOutputTo("500", unknownError));
            }
        };
    }
}

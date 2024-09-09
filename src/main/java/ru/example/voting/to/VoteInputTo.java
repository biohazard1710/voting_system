package ru.example.voting.to;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteInputTo {

    @NotNull(message = "User ID must not be null")
    private Integer userId;

    @NotNull(message = "Restaurant ID must not be null")
    private Integer restaurantId;

}

package ru.example.voting.to;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteInputTo {

    @NotNull(message = "Restaurant ID must not be null")
    private Integer restaurantId;

}

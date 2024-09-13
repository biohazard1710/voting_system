package ru.example.voting.to;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VoteOutputTo {

    private Integer restaurantId;
    private String restaurantName;
    private String dishes;
    private LocalDate voteDate;

}

package ru.example.voting.to;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteTo {

    private Integer userId;
    private Integer restaurantId;

}

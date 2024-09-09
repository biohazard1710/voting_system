package ru.example.voting.to;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MenuOutputTo {

    private Integer restaurantId;
    private String restaurantName;
    private String dishes;
    private LocalDate date;

}

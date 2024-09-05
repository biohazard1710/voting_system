package ru.example.voting.to;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTo {

    private Integer restaurantId;
    private String restaurantName;
    private String dishes;
    private LocalDate date;

}

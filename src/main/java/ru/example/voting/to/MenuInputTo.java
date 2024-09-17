package ru.example.voting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MenuInputTo {

    @NotNull(message = "Restaurant ID cannot be null")
    private Integer restaurantId;

    @NotNull(message = "Menu date cannot be null")
    private LocalDate menuDate;

    @NotBlank(message = "Dishes cannot be blank")
    @Size(min = 20, message = "Dishes description should have at least 20 characters")
    private String dishes;

}

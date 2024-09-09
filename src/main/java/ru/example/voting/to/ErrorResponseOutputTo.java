package ru.example.voting.to;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseOutputTo {

    private String code;
    private String description;

}

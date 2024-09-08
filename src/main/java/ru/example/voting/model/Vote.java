package ru.example.voting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vote")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Vote extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User userId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurantId;

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private LocalDate voteDate;

}

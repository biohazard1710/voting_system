package ru.example.voting.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Setter
@Getter
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "date", nullable = false)
    private LocalDate menuDate;

    @Column(name = "dishes", nullable = false, length = 255)
    private String dishes;

}

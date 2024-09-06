package ru.example.voting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    public Menu(Restaurant restaurant, LocalDate menuDate, String dishes) {
        this.restaurant = restaurant;
        this.menuDate = menuDate;
        this.dishes = dishes;
    }

}

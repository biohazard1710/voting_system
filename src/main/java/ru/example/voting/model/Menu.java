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
public class Menu extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "date", nullable = false)
    private LocalDate menuDate;

    @Column(name = "dishes", nullable = false)
    private String dishes;

    public Menu(Integer id, Restaurant restaurant, LocalDate menuDate, String dishes) {
        super(id);
        this.restaurant = restaurant;
        this.menuDate = menuDate;
        this.dishes = dishes;
    }
}

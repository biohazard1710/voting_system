package ru.example.voting.model;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    public Menu() {
    }

    public Menu(Restaurant restaurant, LocalDate menuDate, String dishes) {
        this.restaurant = restaurant;
        this.menuDate = menuDate;
        this.dishes = dishes;
    }

    public Menu(Integer id, Restaurant restaurant, LocalDate menuDate, String dishes) {
        this.id = id;
        this.restaurant = restaurant;
        this.menuDate = menuDate;
        this.dishes = dishes;
    }

    public Integer getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public String getDishes() {
        return dishes;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", restaurant=" + restaurant +
                ", menuDate=" + menuDate +
                ", dishes='" + dishes + '\'' +
                '}';
    }
}

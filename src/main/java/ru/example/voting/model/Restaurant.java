package ru.example.voting.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus;

    public Restaurant() {}

    public Restaurant(String name) {
        this.name = name;
    }

    public Restaurant(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}

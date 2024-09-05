package ru.example.voting.to;

import java.time.LocalDate;
import java.util.Objects;

public class MenuTo {

    private Integer restaurantId;
    private String restaurantName;
    private String dishes;
    private LocalDate date;

    public MenuTo(Integer restaurantId, String restaurantName, String dishes, LocalDate date) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.dishes = dishes;
        this.date = date;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDishes() {
        return dishes;
    }

    public void setDishes(String dishes) {
        this.dishes = dishes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuTo menuTo = (MenuTo) o;
        return Objects.equals(restaurantId, menuTo.restaurantId) && Objects.equals(restaurantName, menuTo.restaurantName) && Objects.equals(dishes, menuTo.dishes) && Objects.equals(date, menuTo.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, restaurantName, dishes, date);
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", dishes='" + dishes + '\'' +
                ", date=" + date +
                '}';
    }
}

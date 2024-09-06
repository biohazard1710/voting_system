package ru.example.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    List<Menu> findByMenuDate(LocalDate date);

}

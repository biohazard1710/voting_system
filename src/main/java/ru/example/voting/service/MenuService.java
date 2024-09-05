package ru.example.voting.service;

import org.springframework.stereotype.Service;
import ru.example.voting.model.Menu;
import ru.example.voting.repository.MenuRepository;
import ru.example.voting.to.MenuTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuTo> getTodayMenus() {
        LocalDate today = LocalDate.now();
        List<Menu> menus = menuRepository.findByMenuDate(today);

        return menus.stream()
                .map(menu -> new MenuTo(
                        menu.getRestaurant().getId(),
                        menu.getRestaurant().getName(),
                        menu.getDishes(),
                        menu.getMenuDate()
                ))
                .collect(Collectors.toList());
    }
}

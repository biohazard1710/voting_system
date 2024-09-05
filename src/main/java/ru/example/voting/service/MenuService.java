package ru.example.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.voting.model.Menu;
import ru.example.voting.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public Map<String, List<String>> getTodayMenus() {
        LocalDate today = LocalDate.now();
        List<Menu> menus = menuRepository.findMenusByDate(today);

        return menus.stream()
                .collect(Collectors.groupingBy(
                        menu -> menu.getRestaurant().getName(),
                        Collectors.mapping(Menu::getDishes, Collectors.toList())
                ));
    }
}

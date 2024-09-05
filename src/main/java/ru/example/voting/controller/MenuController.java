package ru.example.voting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.voting.service.MenuService;
import ru.example.voting.to.MenuTo;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class MenuController {

    private MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menus")
    public List<MenuTo> getTodayMenus() {
        return menuService.getTodayMenus();
    }

}

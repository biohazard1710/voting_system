package ru.example.voting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.voting.service.MenuService;
import ru.example.voting.to.MenuTo;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Menu Controller", description = "Controller for managing restaurant menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menus")
    @Operation(summary = "Get today's menus", description = "Retrieves the menus for today grouped by restaurant")
    public List<MenuTo> getTodayMenus() {
        return menuService.getTodayMenus();
    }

}

package ru.example.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.voting.service.MenuService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/menus")
    public Map<String, List<String>> getTodayMenus() {
        return menuService.getTodayMenus();
    }

}

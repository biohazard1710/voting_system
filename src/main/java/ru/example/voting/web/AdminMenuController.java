package ru.example.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.example.voting.service.MenuService;
import ru.example.voting.to.MenuInputTo;

@RestController
@RequestMapping("/api/admin/restaurants")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Admin Menu Controller", description = "Controller for managing restaurant menus by admin")
public class AdminMenuController {

    private static final String SUCCESS_CREATED = "Menu successfully created for restaurant with id %d";
    private static final String SUCCESS_UPDATED = "Menu with id %d successfully updated";

    private final MenuService menuService;

    public AdminMenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/menu")
    @Operation(summary = "Create menu for a restaurant", description = "Allows admin to create a menu for a specific restaurant")
    public ResponseEntity<String> createMenu(@Valid @RequestBody MenuInputTo menuInputTom) {
        menuService.createMenu(menuInputTom.getRestaurantId(), menuInputTom.getMenuDate(), menuInputTom.getDishes());
        String message = String.format(SUCCESS_CREATED, menuInputTom.getRestaurantId());
        return ResponseEntity.ok(message);
    }

    @PutMapping("/menu/{menu_id}")
    @Operation(summary = "Update menu", description = "Allows admin to update the dishes of a menu for a specific restaurant")
    public ResponseEntity<String> updateMenu(@PathVariable("menu_id") Integer menuId,
                                             @RequestParam String dishes) {
        menuService.updateMenu(menuId, dishes);
        String message = String.format(SUCCESS_UPDATED, menuId);
        return ResponseEntity.ok(message);
    }

}

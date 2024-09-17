package ru.example.voting.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.voting.model.Menu;
import ru.example.voting.model.Restaurant;
import ru.example.voting.repository.MenuRepository;
import ru.example.voting.repository.RestaurantRepository;
import ru.example.voting.to.MenuOutputTo;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class MenuService {

    protected static final String RESTAURANT_NOT_FOUND = "Restaurant with id %d not found";
    protected static final String MENU_NOT_FOUND = "Menu with id %d not found";

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<MenuOutputTo> getTodayMenus() {
        LocalDate today = LocalDate.now();
        log.info("Getting menus for date: {}", today);
        List<Menu> menus = menuRepository.findByMenuDate(today);

        return menus.stream()
                .map(menu -> new MenuOutputTo(
                        menu.getId(),
                        menu.getRestaurant().getId(),
                        menu.getRestaurant().getName(),
                        menu.getDishes(),
                        menu.getMenuDate()
                ))
                .toList();
    }

    public void createMenu(Integer restaurantId, LocalDate menuDate, String dishes) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        Menu menu = new Menu(restaurant, menuDate, dishes);
        menuRepository.save(menu);
        log.info("Menu created for restaurant with ID {} on date {}", restaurantId, menuDate);
    }

    public void updateMenu(Integer menuId, String dishes) {
        Menu menu = findMenuById(menuId);
        Menu updatedMenu = new Menu(menu.getId(), menu.getRestaurant(), menu.getMenuDate(), dishes);
        menuRepository.save(updatedMenu);
        log.info("Updated menu with ID {}: new dishes - {}", menuId, dishes);
    }

    private Restaurant findRestaurantById(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> {
                    log.error("Restaurant with ID {} not found", restaurantId);
                    String message = String.format(RESTAURANT_NOT_FOUND, restaurantId);
                    return new IllegalArgumentException(message);
                });
    }

    private Menu findMenuById(Integer menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> {
                    log.error("Menu with ID {} not found", menuId);
                    String message = String.format(MENU_NOT_FOUND, menuId);
                    return new IllegalArgumentException(message);
                });
    }

}

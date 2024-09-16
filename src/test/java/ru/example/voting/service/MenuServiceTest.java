package ru.example.voting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.example.voting.model.Menu;
import ru.example.voting.model.Restaurant;
import ru.example.voting.repository.MenuRepository;
import ru.example.voting.repository.RestaurantRepository;
import ru.example.voting.to.MenuOutputTo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuService menuService;

    private LocalDate today;
    private Menu menu1;
    private Menu menu2;
    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        today = LocalDate.now();
        restaurant1 = new Restaurant(1, "Restaurant №1");
        restaurant2 = new Restaurant(2, "Restaurant №2");

        menu1 = new Menu(1, restaurant1, today, "Первое, Второе, Сок");
        menu2 = new Menu(2, restaurant2, today, "Салат, Борщ, Компот");
    }

    @Test
    void testGetTodayMenus() {
        when(menuRepository.findByMenuDate(today)).thenReturn(Arrays.asList(menu1, menu2));

        List<MenuOutputTo> result = menuService.getTodayMenus();

        verify(menuRepository, times(1)).findByMenuDate(today);

        List<MenuOutputTo> expected = Arrays.asList(
                new MenuOutputTo(1, 1, "Restaurant №1", "Первое, Второе, Сок", today),
                new MenuOutputTo(2, 2, "Restaurant №2", "Салат, Борщ, Компот", today)
        );

        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testCreateMenu() {
        Integer restaurantId = 1;
        LocalDate menuDate = today;
        String dishes = "Суп, Второе, Десерт";

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant1));

        menuService.createMenu(restaurantId, menuDate, dishes);

        verify(menuRepository, times(1)).save(argThat(menu ->
                menu.getRestaurant().equals(restaurant1) &&
                        menu.getMenuDate().equals(menuDate) &&
                        menu.getDishes().equals(dishes)
        ));
    }

    @Test
    void testCreateRestaurantNotFound() {
        Integer restaurantId = 999;
        LocalDate menuDate = today;
        String dishes = "Суп, Второе, Десерт";

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        try {
            menuService.createMenu(restaurantId, menuDate, dishes);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo(String.format(MenuService.RESTAURANT_NOT_FOUND, restaurantId));
        }

        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testUpdateMenu() {
        Integer menuId = 1;
        String newDishes = "Обновленное блюдо";

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu1));

        menuService.updateMenu(menuId, newDishes);

        verify(menuRepository, times(1)).save(argThat(menu ->
                menu.getId().equals(menuId) &&
                        menu.getRestaurant().equals(restaurant1) &&
                        menu.getMenuDate().equals(today) &&
                        menu.getDishes().equals(newDishes)
        ));
    }

    @Test
    void testUpdateMenuNotFound() {
        Integer menuId = 999;
        String newDishes = "Обновленное блюдо";

        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        try {
            menuService.updateMenu(menuId, newDishes);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo(String.format(MenuService.MENU_NOT_FOUND, menuId));
        }

        verify(menuRepository, never()).save(any(Menu.class));
    }

}


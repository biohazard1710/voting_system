package ru.example.voting.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import ru.example.voting.model.*;
import ru.example.voting.repository.MenuRepository;
import ru.example.voting.to.MenuTo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    private LocalDate today;
    private Menu menu1;
    private Menu menu2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        today = LocalDate.now();
        Restaurant restaurant1 = new Restaurant(1, "Restaurant №1");
        Restaurant restaurant2 = new Restaurant(2, "Restaurant №2");

        menu1 = new Menu(restaurant1, today, "Первое, Второе, Сок");
        menu2 = new Menu(restaurant2, today, "Салат, Борщ, Компот");
    }

    @Test
    void testGetTodayMenus() {
        when(menuRepository.findByMenuDate(today)).thenReturn(Arrays.asList(menu1, menu2));

        List<MenuTo> result = menuService.getTodayMenus();

        verify(menuRepository, times(1)).findByMenuDate(today);

        List<MenuTo> expected = Arrays.asList(
                new MenuTo(1, "Restaurant №1", "Первое, Второе, Сок", today),
                new MenuTo(2, "Restaurant №2", "Салат, Борщ, Компот", today)
        );

        assertThat(result).containsExactlyInAnyOrder(expected.toArray(new MenuTo[0]));
    }
}

INSERT INTO RESTAURANT(NAME)
VALUES ('Restaurant №1'),
       ('Restaurant №2'),
       ('Restaurant №3');

INSERT INTO MENU(RESTAURANT_ID, DATE, DISHES)
VALUES (1, CURRENT_DATE, 'Первое, Второе, Сок'),
       (2, CURRENT_DATE, 'Салат, Борщ, Компот'),
       (3, CURRENT_DATE, 'Картошка фри, Стейк, Пиво');
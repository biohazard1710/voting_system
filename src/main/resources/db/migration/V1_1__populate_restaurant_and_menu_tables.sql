INSERT INTO RESTAURANT(NAME)
VALUES ('Restaurant №1'),
       ('Restaurant №2'),
       ('Restaurant №3');

INSERT INTO MENU(RESTAURANT_ID, DATE, DISHES)
VALUES (1, CURRENT_DATE, 'Первое - 16.02, Второе - 22.78, Сок - 4.46'),
       (2, CURRENT_DATE, 'Салат - 29.75, Борщ - 11.74, Компот - 2.71'),
       (3, CURRENT_DATE, 'Картошка фри - 8.88, Стейк - 18.50, Пиво - 18.41');
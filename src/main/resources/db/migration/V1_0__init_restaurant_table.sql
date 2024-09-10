DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS menu;

CREATE TABLE restaurant
(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL
);

CREATE TABLE menu
(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INTEGER NOT NULL,
    date DATE NOT NULL,
    dishes VARCHAR(255) NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE,
    CONSTRAINT unique_restaurant_menu_per_day UNIQUE (restaurant_id, date)
);

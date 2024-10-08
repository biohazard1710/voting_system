DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(11) NOT NULL CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE vote
(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id INTEGER NOT NULL,
    menu_id INTEGER NOT NULL,
    vote_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE,
    CONSTRAINT unique_user_vote_per_day UNIQUE (user_id, vote_date)
);

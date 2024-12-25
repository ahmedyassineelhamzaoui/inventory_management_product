DROP DATABASE IF EXISTS inventory;

CREATE DATABASE inventory;

USE inventory;


CREATE TABLE products (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         price DECIMAL(10,2) NOT NULL,
                         category VARCHAR(100),
                         stock INT NOT NULL DEFAULT 0
);

INSERT INTO products (name, price, category, stock) VALUES
                                                       ('Ordinateur Portable', 999.99, 'Électronique', 10),
                                                       ('Chaise de Bureau', 199.99, 'Mobilier', 15),
                                                       ('Machine à Café', 79.99, 'Électroménager', 20),
                                                       ('Tablette Numérique', 299.99, 'Électronique', 8),
                                                       ('Bureau Ajustable', 449.99, 'Mobilier', 5),
                                                       ('Imprimante Laser', 259.99, 'Électronique', 12),
                                                       ('Lampe de Bureau LED', 49.99, 'Éclairage', 25),
                                                       ('Clavier Sans Fil', 89.99, 'Électronique', 30),
                                                       ('Souris Ergonomique', 59.99, 'Électronique', 18),
                                                       ('Écran 27 pouces', 349.99, 'Électronique', 7);
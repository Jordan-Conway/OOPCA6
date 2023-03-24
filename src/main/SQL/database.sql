CREATE DATABASE IF NOT EXISTS oopCA6;

USE oopCA6;

DROP TABLE IF EXISTS gemstones;

CREATE TABLE gemstones (
                           id INT NOT NULL AUTO_INCREMENT,
                           gemName VARCHAR(50),
                           carats DOUBLE,
                           colour VARCHAR(50),
                           clarity ENUM('FL', 'IF', 'VVS1', 'VVS2', 'VS1', 'VS2', 'SI1', 'SI2', 'i1', 'i2', 'i3'),
                           PRIMARY KEY (id)
);

INSERT INTO gemstones(gemName, carats, colour, clarity) VALUES
                                                            ("Amethyst", 2.55, "Siberian", 'VVS2'),
                                                            ("Sapphire", 0.34, "Blue", 'SI1'),
                                                            ("Rose Quartz", 0.65, "Rose", 'SI2');
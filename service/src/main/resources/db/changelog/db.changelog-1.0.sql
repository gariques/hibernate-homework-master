--liquibase formatted sql

--changeset idzhambulov:1
CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL PRIMARY KEY ,
    first_name VARCHAR(64) ,
    last_name VARCHAR(64) ,
    login VARCHAR(64) ,
    password VARCHAR(64) ,
    role VARCHAR(32)
);

--changeset idzhambulov:2
CREATE TABLE IF NOT EXISTS car
(
    id BIGSERIAL PRIMARY KEY ,
    colour varchar(32) ,
    model VARCHAR(32) ,
    price INTEGER ,
    status VARCHAR(32)
);

--changeset idzhambulov:3
CREATE TABLE IF NOT EXISTS client
(
    id BIGINT PRIMARY KEY ,
    driver_license_id VARCHAR(64) ,
    FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
);

--changeset idzhambulov:4
CREATE TABLE IF NOT EXISTS orders
(
    id BIGSERIAL PRIMARY KEY ,
    client_id BIGINT ,
    car_id BIGINT ,
    start_date TIMESTAMP ,
    finish_date TIMESTAMP ,
    FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE ,
    FOREIGN KEY (car_id) REFERENCES car (id) ON DELETE CASCADE
);
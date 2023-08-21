INSERT INTO car (id, colour, model, price, status)
VALUES
    (1, 'white', 'Toyota Camry', 3000, 'AVAILABLE'),
    (2, 'black', 'BMW 7', 5000, 'AVAILABLE'),
    (3, 'white', 'Lexus LX570', 7000, 'NOT_AVAILABLE'),
    (4, 'black', 'Lexus LX570', 7000, 'AVAILABLE');
SELECT SETVAL('car_id_seq', (SELECT MAX(id) FROM car));
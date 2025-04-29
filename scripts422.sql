-- Create Person table
CREATE TABLE Person (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL CHECK (age >= 0),
    has_license BOOLEAN NOT NULL
);

-- Create Car table
CREATE TABLE Car (
    id INT PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0)
);

-- Create relationship table to model multiple people using one car
CREATE TABLE PersonCar (
    person_id INT,
    car_id INT,
    PRIMARY KEY (person_id, car_id),
    FOREIGN KEY (person_id) REFERENCES Person(id),
    FOREIGN KEY (car_id) REFERENCES Car(id)
);
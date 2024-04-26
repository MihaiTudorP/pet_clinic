-- Create the Treatment table
CREATE TABLE Treatment (
                           treatment_id SERIAL PRIMARY KEY,
                           description TEXT NOT NULL,
                           price NUMERIC(10, 2) NOT NULL
);

-- Create the Address table
CREATE TABLE Address (
                         address_id SERIAL PRIMARY KEY,
                         street VARCHAR(255) NOT NULL,
                         number VARCHAR(10) NOT NULL,
                         city VARCHAR(100) NOT NULL
);

-- Create the Owner table
CREATE TABLE Owner (
                       owner_id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       address_id INTEGER,
                       FOREIGN KEY (address_id) REFERENCES Address(address_id)
);

-- Create the Animal table
CREATE TABLE Animal (
                        animal_id SERIAL PRIMARY KEY,
                        owner_id INTEGER NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        species VARCHAR(100) NOT NULL,
                        age INTEGER NOT NULL,
                        gender VARCHAR(50) NOT NULL,
                        race VARCHAR(100) NOT NULL,
                        FOREIGN KEY (owner_id) REFERENCES Owner(owner_id)
);

-- Create the Animal_Treatment link table for the many-to-many relationship between Animals and Treatments
CREATE TABLE Animal_Treatment (
                                  animal_id INTEGER,
                                  treatment_id INTEGER,
                                  FOREIGN KEY (animal_id) REFERENCES Animal(animal_id),
                                  FOREIGN KEY (treatment_id) REFERENCES Treatment(treatment_id),
                                  PRIMARY KEY (animal_id, treatment_id)
);

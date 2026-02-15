CREATE TABLE IF NOT EXISTS classrooms (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    type VARCHAR(50) NOT NULL
    );

INSERT INTO classrooms (name, capacity, type)
VALUES
    ('304', 30, 'lecture'),
    ('305', 20, 'lab'),
    ('Лекционная 1', 100, 'lecture')
    ON CONFLICT DO NOTHING;
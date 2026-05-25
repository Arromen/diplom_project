INSERT INTO classrooms (name, capacity, type)
VALUES
    ('304', 30, 'lecture'),
    ('305', 20, 'lab'),
    ('Лекционная 1', 100, 'lecture')
    ON CONFLICT (name) DO NOTHING;
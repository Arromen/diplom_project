CREATE TABLE classrooms (
                            id BIGSERIAL PRIMARY KEY, -- Исправлено на BIGSERIAL (соответствует Java Long)
                            name VARCHAR(255) NOT NULL UNIQUE,
                            capacity INTEGER NOT NULL,
                            type VARCHAR(255) NOT NULL
);

CREATE TABLE groups (
                        id BIGSERIAL PRIMARY KEY, -- Исправлено на BIGSERIAL
                        name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE teachers (
                          id BIGSERIAL PRIMARY KEY, -- Исправлено на BIGSERIAL
                          full_name VARCHAR(255) NOT NULL
);

CREATE TABLE subjects (
                          id BIGSERIAL PRIMARY KEY, -- Исправлено на BIGSERIAL
                          name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE bookings (
                          id BIGSERIAL PRIMARY KEY, -- Исправлено на BIGSERIAL
                          classroom_id BIGINT NOT NULL REFERENCES classrooms(id) ON DELETE CASCADE,
                          group_id BIGINT REFERENCES groups(id),
                          teacher_id BIGINT REFERENCES teachers(id),
                          subject_id BIGINT REFERENCES subjects(id),
                          booked_by VARCHAR(255) NOT NULL,
                          booking_type VARCHAR(255) NOT NULL,
                          purpose VARCHAR(255) NOT NULL,
                          start_time TIMESTAMP NOT NULL,
                          end_time TIMESTAMP NOT NULL CHECK (end_time > start_time)
);
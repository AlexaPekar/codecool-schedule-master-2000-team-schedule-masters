DROP TABLE IF EXISTS task_uniqueness_checker;
DROP TABLE IF EXISTS tasks_slots;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS slots;
DROP TABLE IF EXISTS "columns";
DROP TABLE IF EXISTS schedules;
DROP TABLE IF EXISTS users;
DROP TYPE IF EXISTS roles;

CREATE TYPE roles AS ENUM('Admin', 'User', 'Guest');

CREATE TABLE users (
id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
password TEXT NOT NULL,
role roles NOT NULL
);

CREATE TABLE schedules (
id SERIAL PRIMARY KEY,
user_id INTEGER,
name TEXT NOT NULL,
FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE "columns" (
id SERIAL PRIMARY KEY,
schedule_id INTEGER NOT NULL,
name TEXT NOT NULL,
FOREIGN KEY (schedule_id) REFERENCES schedules(id)
);

CREATE TABLE slots (
id SERIAL PRIMARY KEY,
column_id INTEGER NOT NULL,
time_range TEXT NOT NULL
);

CREATE TABLE tasks (
id SERIAL PRIMARY KEY,
user_id INTEGER NOT NULL,
name TEXT NOT NULL,
content TEXT,
FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE tasks_slots (
task_id INTEGER NOT NULL,
slot_id INTEGER UNIQUE NOT NULL,
FOREIGN KEY (task_id) REFERENCES tasks(id),
FOREIGN KEY (slot_id) REFERENCES slots(id)
);

CREATE TABLE tasks_schedules (
task_id INTEGER NOT NULL,
schedule_id INTEGER NOT NULL,
FOREIGN KEY (task_id) REFERENCES tasks(id),
FOREIGN KEY (schedule_id) REFERENCES schedules(id),
UNIQUE (task_id, schedule_id)
);

INSERT INTO users (name, password, role)
VALUES ('admin', 'admin', 'Admin');

INSERT INTO schedules (user_id, name)
VALUES ('1', 'TestSchedule');

INSERT INTO columns (schedule_id, name)
VALUES ('1', 'TestColumn');

INSERT INTO slots (column_id, time_range)
VALUES ('1', '7-8');

INSERT INTO tasks (user_id, name, content)
VALUES ('1', 'TestTask', 'TestContentOfTheTask');
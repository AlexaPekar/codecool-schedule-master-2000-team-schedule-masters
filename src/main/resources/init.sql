DROP TABLE IF EXISTS tasks_schedules;
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
VALUES ('1', 'Sima plans');
VALUES ('1', 'SI week');

INSERT INTO columns (schedule_id, name)
VALUES ('1', 'day1');
VALUES ('1', 'day2');
VALUES ('2', 'monday');
VALUES ('2', 'tuesday');
VALUES ('2', 'wednesday');
VALUES ('2', 'thursday');
VALUES ('2', 'friday');
VALUES ('2', 'saturday');
VALUES ('2', 'sunday');

INSERT INTO slots (column_id, time_range)
VALUES ('1', '7-8');
VALUES ('1', '8-9');
VALUES ('1', '9-10');
VALUES ('1', '10-11');
VALUES ('1', '11-12');
VALUES ('1', '12-13');
VALUES ('1', '13-14');
VALUES ('1', '14-15');
VALUES ('1', '15-16');
VALUES ('1', '16-17');
VALUES ('1', '17-18');
VALUES ('1', '18-19');

VALUES ('2', '7-8');
VALUES ('2', '8-9');
VALUES ('2', '9-10');
VALUES ('2', '10-11');
VALUES ('2', '11-12');
VALUES ('2', '12-13');
VALUES ('2', '13-14');
VALUES ('2', '14-15');
VALUES ('2', '15-16');
VALUES ('2', '16-17');
VALUES ('2', '17-18');
VALUES ('2', '18-19');

VALUES ('3', '7-8');
VALUES ('3', '8-9');
VALUES ('3', '9-10');
VALUES ('3', '10-11');
VALUES ('3', '11-12');
VALUES ('3', '12-13');
VALUES ('3', '13-14');
VALUES ('3', '14-15');
VALUES ('3', '15-16');
VALUES ('3', '16-17');
VALUES ('3', '17-18');
VALUES ('3', '18-19');

VALUES ('4', '7-8');
VALUES ('4', '8-9');
VALUES ('4', '9-10');
VALUES ('4', '10-11');
VALUES ('4', '11-12');
VALUES ('4', '12-13');
VALUES ('4', '13-14');
VALUES ('4', '14-15');
VALUES ('4', '15-16');
VALUES ('4', '16-17');
VALUES ('4', '17-18');
VALUES ('4', '18-19');

VALUES ('5', '7-8');
VALUES ('5', '8-9');
VALUES ('5', '9-10');
VALUES ('5', '10-11');
VALUES ('5', '11-12');
VALUES ('5', '12-13');
VALUES ('5', '13-14');
VALUES ('5', '14-15');
VALUES ('5', '15-16');
VALUES ('5', '16-17');
VALUES ('5', '17-18');
VALUES ('5', '18-19');

VALUES ('6', '7-8');
VALUES ('6', '8-9');
VALUES ('6', '9-10');
VALUES ('6', '10-11');
VALUES ('6', '11-12');
VALUES ('6', '12-13');
VALUES ('6', '13-14');
VALUES ('6', '14-15');
VALUES ('6', '15-16');
VALUES ('6', '16-17');
VALUES ('6', '17-18');
VALUES ('6', '18-19');

VALUES ('7', '7-8');
VALUES ('7', '8-9');
VALUES ('7', '9-10');
VALUES ('7', '10-11');
VALUES ('7', '11-12');
VALUES ('7', '12-13');
VALUES ('7', '13-14');
VALUES ('7', '14-15');
VALUES ('7', '15-16');
VALUES ('7', '16-17');
VALUES ('7', '17-18');
VALUES ('7', '18-19');

VALUES ('8', '7-8');
VALUES ('8', '8-9');
VALUES ('8', '9-10');
VALUES ('8', '10-11');
VALUES ('8', '11-12');
VALUES ('8', '12-13');
VALUES ('8', '13-14');
VALUES ('8', '14-15');
VALUES ('8', '15-16');
VALUES ('8', '16-17');
VALUES ('8', '17-18');
VALUES ('8', '18-19');

VALUES ('9', '7-8');
VALUES ('9', '8-9');
VALUES ('9', '9-10');
VALUES ('9', '10-11');
VALUES ('9', '11-12');
VALUES ('9', '12-13');
VALUES ('9', '13-14');
VALUES ('9', '14-15');
VALUES ('9', '15-16');
VALUES ('9', '16-17');
VALUES ('9', '17-18');
VALUES ('9', '18-19');

INSERT INTO tasks (user_id, name, content)
VALUES ('1', 'Drink beer', 'try not to vomit'),
('1', 'Eat chili beans', 'this is a nice activity, tell Imi it tastes good.'),
('1', 'Listen to Kenéz singing', 'feat Hresko Norbi w guitar'),
('1', 'Partying', 'just dance!'),
('1', 'Sleeping', 'not much'),
('1', 'Survive hangover', 'The Bear Grylls way'),
('1', 'Go home', ':('),
('1', 'Practice JavaSrcipt', 'call backs, DOM manipulation, jQuery'),
('1', 'Fail to pratice JavaScript', 'because errors'),
('1', 'Chill', 'you know the drill'),
('1', 'Start implement the assignments', 'start is the hardest part'),
('1', 'Continue implementation', 'try not to give up'),
('1', 'Finishing implementation', 'and forgot to submit'),
('1', 'Submit the assignment', 'because you forgot'),
('1', 'Feed the Tomcat', 'RRRrrrrrr'),
('1', 'Browsing the stackoverflow', 'find the complete solution'),
('1', 'Try not to cry', 'you are beautiful and strong ;)'),
('1', 'Pray for successful compile', 'remember to feed the Tomcat');

INSERT INTO tasks_slots (task_id, slot_id)
VALUES (1, 6),
(2, 8),
(3, 9),
(4, 10),
(5, 11),
(6, 16),
(7, 18),
(8, 27),
(9, 33),
(10, 40),
(11, 46),
(12, 56),
(13, 70),
(14, 83),
(15, 94),
(16, 109);
(17, 113);
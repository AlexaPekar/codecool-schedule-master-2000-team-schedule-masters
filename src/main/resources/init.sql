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
VALUES ('admin', 'admin', 'Admin'),
('Joe', 'admin', 'User'),
('Liz', 'littleduck67', 'User'),
('Josh', 'NeverFindOut', 'User'),
('Samantha', 'mypassword99', 'User'),
('Robert', 'lolkabolka11', 'User'),
('Selena', 'neverForget83', 'User'),
('John', 'IlikeSissy12', 'User'),
('Jessica', 'jess1992', 'User');

INSERT INTO schedules (user_id, name)
VALUES ('1', 'Sima plans'),
('1', 'SI week');

INSERT INTO columns (schedule_id, name)
VALUES ('1', 'day1'),
('1', 'day2'),
('2', 'monday'),
('2', 'tuesday'),
('2', 'wednesday'),
('2', 'thursday'),
('2', 'friday'),
('2', 'saturday'),
('2', 'sunday');

INSERT INTO slots (column_id, time_range)
VALUES ('1', '7-8'),
('1', '8-9'),
('1', '9-10'),
('1', '10-11'),
('1', '11-12'),
('1', '12-13'),
('1', '13-14'),
('1', '14-15'),
('1', '15-16'),
('1', '16-17'),
('1', '17-18'),
('1', '18-19'),

('2', '7-8'),
('2', '8-9'),
('2', '9-10'),
('2', '10-11'),
('2', '11-12'),
('2', '12-13'),
('2', '13-14'),
('2', '14-15'),
('2', '15-16'),
('2', '16-17'),
('2', '17-18'),
('2', '18-19'),

('3', '7-8'),
('3', '8-9'),
('3', '9-10'),
('3', '10-11'),
('3', '11-12'),
('3', '12-13'),
('3', '13-14'),
('3', '14-15'),
('3', '15-16'),
('3', '16-17'),
('3', '17-18'),
('3', '18-19'),

('4', '7-8'),
('4', '8-9'),
('4', '9-10'),
('4', '10-11'),
('4', '11-12'),
('4', '12-13'),
('4', '13-14'),
('4', '14-15'),
('4', '15-16'),
('4', '16-17'),
('4', '17-18'),
('4', '18-19'),

('5', '7-8'),
('5', '8-9'),
('5', '9-10'),
('5', '10-11'),
('5', '11-12'),
('5', '12-13'),
('5', '13-14'),
('5', '14-15'),
('5', '15-16'),
('5', '16-17'),
('5', '17-18'),
('5', '18-19'),

('6', '7-8'),
('6', '8-9'),
('6', '9-10'),
('6', '10-11'),
('6', '11-12'),
('6', '12-13'),
('6', '13-14'),
('6', '14-15'),
('6', '15-16'),
('6', '16-17'),
('6', '17-18'),
('6', '18-19'),

('7', '7-8'),
('7', '8-9'),
('7', '9-10'),
('7', '10-11'),
('7', '11-12'),
('7', '12-13'),
('7', '13-14'),
('7', '14-15'),
('7', '15-16'),
('7', '16-17'),
('7', '17-18'),
('7', '18-19'),

('8', '7-8'),
('8', '8-9'),
('8', '9-10'),
('8', '10-11'),
('8', '11-12'),
('8', '12-13'),
('8', '13-14'),
('8', '14-15'),
('8', '15-16'),
('8', '16-17'),
('8', '17-18'),
('8', '18-19'),

('9', '7-8'),
('9', '8-9'),
('9', '9-10'),
('9', '10-11'),
('9', '11-12'),
('9', '12-13'),
('9', '13-14'),
('9', '14-15'),
('9', '15-16'),
('9', '16-17'),
('9', '17-18'),
('9', '18-19');

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
('1', 'Pray for successful compile', 'remember to feed the Tomcat'),

('2', 'Drink beer', 'try not to vomit'),
('2', 'Eat chili beans', 'this is a nice activity, tell Imi it tastes good.'),
('2', 'Listen to Kenéz singing', 'feat Hresko Norbi w guitar'),
('2', 'Partying', 'just dance!'),
('2', 'Sleeping', 'not much'),
('2', 'Survive hangover', 'The Bear Grylls way'),
('2', 'Go home', ':('),
('2', 'Practice JavaSrcipt', 'call backs, DOM manipulation, jQuery'),
('2', 'Fail to pratice JavaScript', 'because errors'),
('2', 'Chill', 'you know the drill'),
('2', 'Start implement the assignments', 'start is the hardest part'),
('2', 'Continue implementation', 'try not to give up'),
('2', 'Finishing implementation', 'and forgot to submit'),
('2', 'Submit the assignment', 'because you forgot'),
('2', 'Feed the Tomcat', 'RRRrrrrrr'),
('2', 'Browsing the stackoverflow', 'find the complete solution'),
('2', 'Try not to cry', 'you are beautiful and strong ;)'),
('2', 'Pray for successful compile', 'remember to feed the Tomcat');

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
(16, 98),
(17, 105);
INSERT INTO PERSON (name, age, email)
VALUES ('Пагасян Игорь Леонидович', 1992, 'test1@bk.ru'),
       ('Иванов Петр Михайлович', 1944, 'test2@bk.ru'),
       ('Гойда Даздраперма Филипповна', 1928, 'test3@bk.ru'),
       ('Михайлов Станислав Дабулачадович', 1988, 'test4@bk.ru'),
       ('Скуфов Айрат Балдович', 1990, 'test5@bk.ru');

INSERT INTO Person (name, age, email) VALUES ('Tom', 25, 'tom@mail.com');
INSERT INTO Person (name, age, email) VALUES ('Bob', 31, 'bob1@mail.com');
INSERT INTO Person (name, age, email) VALUES ('Bob2', 53, 'bob2@mail.com');
INSERT INTO Person (name, age, email) VALUES ('Bob3', 20, 'bob3@mail.com');
INSERT INTO Person (name, age, email) VALUES ('Katy', 14, 'katy@mail.com');

INSERT INTO Item (person_id, item_name) VALUES(1, 'Airpods');
INSERT INTO Item (person_id, item_name) VALUES(1, 'Playstation');
INSERT INTO Item (person_id, item_name) VALUES(1, 'TV');
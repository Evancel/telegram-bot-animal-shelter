--liquibase formatted sql

-- changeset amargolina:5
INSERT INTO animal_shelter(name,country,city,address,working_hours,pass_rules_id,rules,animal_type)
VALUES('Приют1 для кошек','KZ','ASTANA','адрес1','пн-пт 09:00 - 19:00; сб,вс 10:00 - 20:00',null,
'Наши правила:','CAT'),
('Приют2 для кошек','KZ','ASTANA','адрес2','пн-пт 09:00 - 18:00; сб,вс 12:00 - 17:00',null,
'Наши правила: 1. мы любим животных.','CAT'),
('Приют3 для собак','KZ','ALMATY','адрес3','пн-пт 09:00 - 18:00; сб,вс 12:00 - 17:00',null,
'Наши правила: какие-то правила','DOG'),
('Приют4 для собак','KZ','ARKALYK','адрес4','пн-вс 24/7',null,
'Наши правила: для всех','DOG');
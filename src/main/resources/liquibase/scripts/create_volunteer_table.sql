--liquibase formatted sql

-- changeset amargolina:6
create table volunteer(
id serial primary key,
login text,
name text,
is_available boolean
);
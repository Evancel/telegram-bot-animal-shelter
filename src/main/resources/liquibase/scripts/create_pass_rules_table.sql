--liquibase formatted sql

-- changeset amargolina:2
create table pass_rules(
id serial primary key,
file_path text,
file_size bigint,
media_type text,
data bytea
);
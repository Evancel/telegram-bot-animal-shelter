--liquibase formatted sql

-- changeset amargolina:7
INSERT INTO volunteer(id,login,name,is_available)
VALUES(1,'https://t.me/EvancelBot','volunteer_chat',true);
--liquibase formatted sql
-- changeset amargolina:3
CREATE TABLE animal_shelter (
                          id SERIAL primary key,
                          name TEXT,
                          country   TEXT,
                          city TEXT,
                          address TEXT,
                          working_hours TEXT,
                          pass_rules_id BIGINT,
                          rules TEXT,
                          animal_type   TEXT,
                          CONSTRAINT fk_pass_rules_id
                          FOREIGN KEY (pass_rules_id)
                          REFERENCES pass_rules(id)
);
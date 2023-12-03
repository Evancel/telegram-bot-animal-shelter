--liquibase formatted sql

-- changeset amargolina:4
CREATE TABLE curr_choice (
                          id SERIAL primary key,
                          current_date_time TIMESTAMP,
                          app_user_id BIGINT,
                          animal_shelter_id BIGINT,
                          CONSTRAINT fk_app_user_id
                            FOREIGN KEY (app_user_id)
                            REFERENCES app_user(id),
                          CONSTRAINT fk_animal_shelter_id
                            FOREIGN KEY (animal_shelter_id)
                            REFERENCES animal_shelter(id)
);
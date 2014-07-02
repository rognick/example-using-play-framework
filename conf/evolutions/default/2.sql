# --- !Ups

ALTER TABLE users
ADD age INTEGER NOT NULL

# --- !Downs

DROP TABLE IF EXISTS users;
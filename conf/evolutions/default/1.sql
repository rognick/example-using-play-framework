# --- !Ups

CREATE TABLE users (
  email      VARCHAR(50) NOT NULL PRIMARY KEY,
  first_name VARCHAR(50) NOT NULL,
  last_name  VARCHAR(50) NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS users;
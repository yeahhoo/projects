DROP TABLE users IF EXISTS;
DROP TABLE authorities IF EXISTS;

CREATE TABLE users (
  id INTEGER PRIMARY KEY,
  username VARCHAR(30),
  password VARCHAR(30),
  enabled boolean not null DEFAULT FALSE,
  email  VARCHAR(50)
);

create table authorities (
	username VARCHAR(30) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username, authority);
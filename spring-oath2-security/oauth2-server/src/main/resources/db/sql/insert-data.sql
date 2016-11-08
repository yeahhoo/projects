INSERT INTO users VALUES (1, 'me', 'me', true, 'me@gmail.com');
INSERT INTO users VALUES (2, 'admin', 'admin', true, 'admin@gmail.com');
INSERT INTO users VALUES (3, 'dba', 'dba', true, 'dba@gmail.com');
INSERT INTO users VALUES (4, 'user', 'user', true, 'user@gmail.com');

INSERT INTO authorities VALUES ('me', 'DBA');
INSERT INTO authorities VALUES ('me', 'ADMIN');
INSERT INTO authorities VALUES ('admin', 'ADMIN');
INSERT INTO authorities VALUES ('dba', 'DBA');
INSERT INTO authorities VALUES ('user', 'USER');
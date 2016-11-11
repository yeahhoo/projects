-- data for users
INSERT INTO users VALUES (user_sequence.nextVal, 'me', 'me', true);
INSERT INTO users VALUES (user_sequence.nextVal, 'admin', 'admin', true);
INSERT INTO users VALUES (user_sequence.nextVal, 'dba', 'dba', true);
INSERT INTO users VALUES (user_sequence.nextVal, 'user', 'user', true);

INSERT INTO authorities VALUES ('me', 'DBA');
INSERT INTO authorities VALUES ('me', 'ADMIN');
INSERT INTO authorities VALUES ('admin', 'ADMIN');
INSERT INTO authorities VALUES ('dba', 'DBA');
INSERT INTO authorities VALUES ('user', 'USER');

-- data for clients
INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types) VALUES ('foo', 'foosecret', 'openid,create', 'authorization_code,refresh_token,password');
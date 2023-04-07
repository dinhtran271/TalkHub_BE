CREATE TABLE user
(
  id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(2048) NOT NULL,
  token VARCHAR(2048),
  role INT,
  status INT,
  createTime BIGINT NOT NULL
);
CREATE UNIQUE INDEX user_username_uindex ON user (username);
CREATE UNIQUE INDEX user_token_uindex ON user (token);


CREATE TABLE th_user
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username VARCHAR(128) NOT NULL,
    password VARCHAR(2048) NOT NULL,
    role INT DEFAULT 0,
    status INT,
    createTime BIGINT NOT NULL,
    refresh_token VARCHAR(2048),
    token_exp_time BIGINT
);
CREATE UNIQUE INDEX user_username_uindex ON th_user(username);
CREATE UNIQUE INDEX user_token_uindex ON th_user(refresh_token);

CREATE TABLE th_login_session
(
    userid BIGINT NOT NULL,
    device_id VARCHAR(256) NOT NULL,
    token VARCHAR(2048),
    token_exp_time BIGINT,
    session_info VARCHAR(2048),
    PRIMARY KEY(userid, device_id)
);
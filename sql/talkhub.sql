CREATE TABLE profile 
(
    userid BIGINT PRIMARY KEY NOT NULL,
    nick_name VARCHAR(50) NOT NULL,
    avatar VARCHAR(255),
    age INT,
    start BIGINT DEFAULT 0
);

CREATE TABLE category 
(
    id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE topic 
(
    id BIGINT PRIMARY KEY NOT NULL,
    userid BIGINT NOT NULL,
    name VARCHAR(250) NOT NULL,
    content JSON NOT NULL,
    create_time BIGINT NOT NULL,
    likes BIGINT DEFAULT 0,
    view BIGINT DEFAULT 0
);

CREATE TABLE post 
(
    id BIGINT PRIMARY KEY NOT NULL,
    userid BIGINT NOT NULL,
    topicid BIGINT NOT NULL,
    content JSON NOT NULL,
    create_time BIGINT NOT NULL,
    likes BIGINT DEFAULT 0
);

CREATE TABLE tag
(
    id BIGINT PRIMARY KEY NOT NULL,
    topicid BIGINT NOT NULL,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE follow
(
    userid BIGINT NOT NULL,
    otherid BIGINT NOT NULL,
    PRIMARY KEY(userid, otherid)
);

CREATE TABLE bookmark
(
    userid BIGINT NOT NULL,
    topicid BIGINT NOT NULL,
    PRIMARY KEY (userid, topicid)
);



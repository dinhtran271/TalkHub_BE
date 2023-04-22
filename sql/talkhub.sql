DROP TABLE if exists th_profile ;

CREATE TABLE th_profile 
(
    userid BIGINT PRIMARY KEY NOT NULL,
    nick_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    avatar VARCHAR(255),
    age INT,
    likes BIGINT DEFAULT 0
);

DROP TABLE if exists th_category ;

CREATE TABLE th_category 
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL
);

DROP TABLE if exists th_topic ;

CREATE TABLE th_topic 
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    categoryid BIGINT NOT NULL,
    userid BIGINT NOT NULL,
    title VARCHAR(250) NOT NULL,
    content JSON NOT NULL,
    img JSON,
    create_time BIGINT NOT NULL,
    likes BIGINT DEFAULT 0,
    view BIGINT DEFAULT 0
);


DROP TABLE if exists th_post ;
    
CREATE TABLE th_post 
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    userid BIGINT NOT NULL,
    topicid BIGINT NOT NULL,
    content VARCHAR(2048) NOT NULL,
    create_time BIGINT NOT NULL,
    likes BIGINT DEFAULT 0
);

DROP TABLE if exists th_tag ;

CREATE TABLE th_tag
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    topicid BIGINT NOT NULL,
    name VARCHAR(30) NOT NULL
);

DROP TABLE if exists th_follow ;

CREATE TABLE th_follow
(
    userid BIGINT NOT NULL,
    otherid BIGINT NOT NULL,
    PRIMARY KEY(userid, otherid)
);

DROP TABLE if exists th_bookmark ;

CREATE TABLE th_bookmark
(
    userid BIGINT NOT NULL,
    topicid BIGINT NOT NULL,
    PRIMARY KEY (userid, topicid)
);

CREATE TABLE th_notification
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    userid BIGINT NOT NULL,
    otherid BIGINT NOT NULL,
    topicid BIGINT NOT NULL,
    create_time BIGINT NOT NULL
);



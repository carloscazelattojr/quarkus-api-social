CREATE DATABASE QUARKUS-SOCIAL;

CREATE TABLE TB_USERS
(
   ID BIGSERIAL NOT NULL PRIMARY KEY,
	NAME VARCHAR(100) NOT NULL,
	AGE INTEGER NOT NULL
)

CREATE TABLE TB_POSTS
(
   ID BIGSERIAL NOT NULL PRIMARY KEY,
	POST_TEXT VARCHAR(150) NOT NULL,
	DATA_TIME TIMESTAMP,
	USER_ID BIGINT NOT NULL REFERENCES TB_USERS(ID)
)

CREATE TABLE TB_FOLLOWERS(
	ID BIGSERIAL NOT NULL PRIMARY KEY,
	USER_ID BIGINT NOT NULL REFERENCES TB_USERS(ID),
	FOLLOWER_ID BIGINT NOT NULL REFERENCES TB_USERS(ID)
)
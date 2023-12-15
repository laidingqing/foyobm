-- 用户
CREATE TABLE IF NOT EXISTS users(
    id bigserial NOT NULL PRIMARY KEY,
    email varchar(20) not null,
    user_name varchar(50),
    password varchar(500) not null,
    status bigint not null default 0,
    fail_count bigint not null default 0,
    last_logined timestamp,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
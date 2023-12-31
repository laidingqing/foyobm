-- 用户
CREATE TABLE IF NOT EXISTS activities(
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint,
    name varchar(50),
    score bigint not null default 0,
    title varchar(100) not null,
    project_id bigint not null,
    ext_id varchar(100),
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- 用户
CREATE TABLE IF NOT EXISTS activities(
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint not null,
    score bigint not null default 0,
    title varchar(100) not null,
    project_id bigint not null,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
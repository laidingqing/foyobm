-- 用户
CREATE TABLE IF NOT EXISTS points(
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint,
    points bigint not null default 0,
    catalog varchar(100) not null default 'manual',
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
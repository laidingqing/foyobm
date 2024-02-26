-- 用户
CREATE TABLE IF NOT EXISTS points(
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint,
    company_id bigint,
    points bigint not null default 0,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
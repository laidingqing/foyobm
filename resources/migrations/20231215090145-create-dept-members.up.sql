-- 用户
CREATE TABLE IF NOT EXISTS dept_members(
    id bigserial NOT NULL PRIMARY KEY,
    company_id bigint not null,
    user_id bigint not null,
    dept_id bigint not null,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
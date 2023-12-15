-- 用户
CREATE TABLE IF NOT EXISTS departments(
    id bigserial NOT NULL PRIMARY KEY,
    company_id bigint not null,
    name varchar(50) not null,
    position bigint not null default 0,
    code varchar(50) not null,
    parent bigint not null default 0,
    manage_id bigint,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
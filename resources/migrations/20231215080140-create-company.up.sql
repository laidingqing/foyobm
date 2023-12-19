-- 用户
CREATE TABLE IF NOT EXISTS companies(
    id bigserial NOT NULL PRIMARY KEY,
    name varchar(100) not null,
    abbr varchar(50),
    address varchar(200),
    telphone varchar(100),
    member_count bigint not null default 0,
    dept_count bigint not null default 0,
    create_by bigint not null,
    status bigint not null default 0,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
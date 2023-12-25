-- 用户
CREATE TABLE IF NOT EXISTS projects(
    id bigserial NOT NULL PRIMARY KEY,
    name varchar(100) not null,
    datalog varchar(100) not null,
    description varchar(200),
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
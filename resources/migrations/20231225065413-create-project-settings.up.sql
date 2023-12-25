-- 用户
CREATE TABLE IF NOT EXISTS project_settings(
    id bigserial NOT NULL PRIMARY KEY,
    project_id bigint not null,
    datalog varchar(100) not null,
    name varchar(100) not null,
    value varchar(200) not null,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
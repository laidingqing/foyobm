-- 用户
CREATE TABLE IF NOT EXISTS project_dicts(
    id bigserial NOT NULL PRIMARY KEY,
    description varchar(100),
    classv varchar(50) not null,
    code varchar(50) not null,
    title varchar(50) not null,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
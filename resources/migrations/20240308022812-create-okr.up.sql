-- OKR
CREATE TABLE IF NOT EXISTS okrs(
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint not null,
    company_id bigint not null,
    title varchar(100) not null,
    expire timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    process NUMERIC(10,2) not null default 0.00,
    priority bigint not null default 0,
    cycle varchar(10) not null default 'm',
    started timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ended timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
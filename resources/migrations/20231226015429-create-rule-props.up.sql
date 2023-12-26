CREATE TABLE IF NOT EXISTS rule_props(
    id bigserial NOT NULL PRIMARY KEY,
    rule_id bigint not null,
    field varchar(100),
    operator varchar(20) not null, -- [>, >= , <, <=, !=]
    target varchar(100) not null,
    score bigint not null default 0,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
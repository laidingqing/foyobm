-- KRs
CREATE TABLE IF NOT EXISTS key_results(
    id bigserial NOT NULL PRIMARY KEY,
    o_id bigint not null,
    user_id bigint not null,
    company_id bigint not null,
    title varchar(100) not null,
    tv NUMERIC(10,2) not null default 0, -- 目标
    cv NUMERIC(10,2) not null default 0, -- 当前
    unit varchar(10) not null,
    expire timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
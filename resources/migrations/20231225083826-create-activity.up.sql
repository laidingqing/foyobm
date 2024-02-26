-- 用户
CREATE TABLE IF NOT EXISTS activities(
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint,
    name varchar(50), -- 姓名
    score bigint not null default 0,
    pre_score bigint not null default 0, -- 变动时上次积分
    title varchar(100) not null,
    catalog varchar(100) not null, -- plugin name
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
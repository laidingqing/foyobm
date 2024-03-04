-- 用户信息
CREATE TABLE IF NOT EXISTS profiles(
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint not null,
    education varchar(20), -- 学历
    employed timestamp without time zone, -- 入职时间
    
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
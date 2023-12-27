-- 用户
CREATE TABLE IF NOT EXISTS rules(
    id bigserial NOT NULL PRIMARY KEY,
    project_id bigint not null,
    title varchar(50) not null,
    event_key varchar(100) not null, -- 与config.edn配置中的event-rule-keys对应
    status bigint not null default 1, -- 0禁用, 1启用
    score bigint not null default 0, -- 默认积分值
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
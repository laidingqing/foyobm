-- 用户
CREATE TABLE IF NOT EXISTS users(
    id bigserial NOT NULL PRIMARY KEY,
    email varchar(20) not null,
    user_name varchar(50),
    password varchar(500) not null,
    status bigint not null default 0,
    fail_count bigint not null default 0,
    last_logined timestamp,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 企业
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

-- 部门或分组
CREATE TABLE IF NOT EXISTS departments(
    id bigserial NOT NULL PRIMARY KEY,
    company_id bigint not null,
    name varchar(50) not null,
    position bigint not null default 0,
    code varchar(50) not null,
    parent bigint not null default 0,
    manage_id bigint,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 分组成员
CREATE TABLE IF NOT EXISTS dept_members(
    id bigserial NOT NULL PRIMARY KEY,
    company_id bigint not null,
    user_id bigint not null,
    dept_id bigint not null,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 管理用户
CREATE TABLE IF NOT EXISTS company_admins(
    id bigserial NOT NULL PRIMARY KEY,
    company_id bigint not null,
    user_id bigint not null,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 积分事件
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

-- 积分
CREATE TABLE IF NOT EXISTS points(
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint,
    company_id bigint,
    points bigint not null default 0,
    created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
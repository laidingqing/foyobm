# Data

```clojure
;; 项目设置
{
    :name "JIRA/WeCom/Ding"
    :datalog "webhook/timer/"
    :settings {:cron_exp "* * 1 * * * "
               :x_ingest_event ""
               :key ""
               :secret ""
               :corp_id ""
               }
}
```

```clojure
;; 项目类别配置，持续接入不同数据来源
{
    :JIRA {:description "任务管理，用于采集工作量"
           :datalog :webhook
           :uri "/webhooks/jira/"}
    :WeCom "企业微信考勤管理，用于采集出勤"
    :Ding "钉钉考勤管理，用于采集出勤"
}
```

```clojure
;; 积分规则配置
```
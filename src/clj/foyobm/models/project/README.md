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

```clojure
;; 积分类型
{
    :basis      "固定积分"
    :regular    "规则积分"
    :reporting  "申报积分"
}
```

## JIRA积分规则设置

* 产生缺陷 -> {:name "缺陷" :title "产生缺陷" :threshold 0 :score -2}
* 返工次数 -> {:name "返工" :title "多次返工" :threshold 2 :score -1}
* 延期完成
* 完成任务
* 提前完成
* 工时登记
* 
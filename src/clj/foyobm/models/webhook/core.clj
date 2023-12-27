(ns foyobm.models.webhook.core
  (:require [taoensso.timbre :as log]))



(defn extract-necessary-jira-fields
  "提取必要数据与字段"
  [issue-data rules]
  (let [{:keys [issue]} issue-data
        {:keys [key fields]} issue
        ;; customfield_10200 customfield_10300该域为自定义自段，如果通用则应使用配置, TODO
        {:keys [issuetype assignee status updated customfield_10200 customfield_10300]} fields
        issue-type (-> issuetype :name)
        assign-name (-> assignee :displayName)
        status-name (-> status :name)
        complexity-name (-> customfield_10200 :value) ;; 复杂度
        bug-grade-name (-> customfield_10300 :value)  ;; 缺陷等级
        activity {:assignee assign-name :jira-key key :finished updated}
        is-bug (= issue-type "故障")
        is-finished (= status-name "完成")
        activities (cond-> []
                     is-bug (conj (assoc activity :event-type "bug" :complexity (or complexity-name "容易") :bug-grade (or bug-grade-name "细微错误")))
                     is-finished (conj (assoc activity :event-type "finish" :complexity (or complexity-name "容易"))))]
    (log/info activities)))

(comment
  
  )
(ns foyobm.models.webhook.core
  (:require [taoensso.timbre :as log]))


(def bug-grade-jira {:key "customfield_10300" :values ["容易" "一般" "较复杂" "复杂"]})
(def complexity-jira {:key "customfield_10200" :values ["细微错误" "一般错误" "严重错误" "致命错误"]})

(defn match-rule? 
  "根据定义的规则属性匹配"
  [activity rule-prop]
  (let [event-type (:event-type activity)
        field (cond
                (= event-type "finish") :complexity
                (= event-type "bug") :bug-grade
                :else (:field rule-prop))
        operator (:operator rule-prop)
        target (:target rule-prop)
        value (get activity field)]
    (cond
      (= operator "=") (= value target)
      (= operator "!=") (not= value target)
      (= operator ">") (> value target)
      (= operator "<") (< value target)
      :else false)))

(defn evaluate-score 
  "评估积分"
  [props activity rule] 
  (let [matching-rule (first (filter (partial match-rule? activity) props))
        match-rule-id (-> matching-rule :rule_id)
        match-rule-score (-> matching-rule :score)
        match-rule-prop-id (-> matching-rule :id)
        rule-id (-> rule :id)
        rule-score (-> rule :score)]
    (if matching-rule
      {:rule-id match-rule-id :score match-rule-score :prop-id match-rule-prop-id}
      {:rule-id rule-id :score rule-score :prop-id nil})
    ))

(defn evaluate-jira-activity-score
  "根据活动日志评估积分, TODO"
  [rules activities]
  (let [rule-map (into {} (for [rule rules]
                            [(:event_key rule) rule]))
        calculate-score (fn [activity] (let [event-type (:event-type activity)
                                             rule (get rule-map event-type)
                                             props (when rule (-> rule :props))
                                             {:keys [rule-id score prop-id]} (evaluate-score props activity rule)
                                             cal-activity (assoc activity :score score :rule-id rule-id :prop-id prop-id)]
                                         cal-activity))
        activities-score (vec (map calculate-score activities))]
    (->> activities-score
         (filter #(not (= (:score %) 0))))))


(defn extract-necessary-jira-fields
  "提取必要数据与字段"
  [issue-data rules]
  (let [{:keys [issue]} issue-data
        {:keys [key fields]} issue
        {:keys [issuetype assignee status updated ]} fields
        customfield_10200 (-> fields (:key complexity-jira))
        customfield_10300 (-> fields (:key bug-grade-jira))
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
  (def rules [{:id 1 :event_key "finish" :score 10 :props [{:id 1 :rule_id 1 :field "customfield_10200" :operator "=" :target "容易" :score 20}]}])
  (def activities [{:event-type "finish" :assignee "赖sir" :jira-key "CKMRO-1100" :complexity "容易"}])
  (evaluate-jira-activity-score rules activities)
  )
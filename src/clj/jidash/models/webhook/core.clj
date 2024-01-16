(ns jidash.models.webhook.core
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
      {:rule-id rule-id :score (or rule-score 0) :prop-id nil})
    ))

(defn evaluate-jira-activity-score
  "根据活动日志评估积分, TODO"
  [activities rules]
  (log/info "start evaluate jira score")
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


(defn- wrap-activity-title [activity]
  (let [{:keys [jira-key event-title complexity]} activity
        title (format event-title jira-key complexity)
        activity (assoc activity :title title)]
    (log/info activity) 
    activity))

(defn extract-necessary-jira-fields
  "提取必要数据与字段"
  [issue-data rules]
  (let [{:keys [issue]} issue-data
        {:keys [key fields]} issue
        {:keys [issuetype assignee status updated]} fields
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
                     is-finished (conj (assoc activity :event-title "完成任务[%s],复杂度[%s]" :event-type "finish" :complexity (or complexity-name "容易"))))
        activities (vec (evaluate-jira-activity-score activities rules))]
    (map (fn [activity] (-> activity
                            (select-keys [:assignee :jira-key :score :event-title :complexity])
                            (wrap-activity-title)
                            (select-keys [:assignee :title :score :rule-id :prop-id]))) activities)))



(comment
  (def rules [{:id 1 :event_key "finish" :score 10 :props [{:id 1 :rule_id 1 :field "customfield_10200" :operator "=" :target "容易" :score 20}]}])
  (def activities [{:event-type "finish" :assignee "赖sir" :jira-key "CKMRO-1100" :complexity "容易"}])
  (def issue {:issue {
                     :key "CKMRO-2411",
                     :fields {:issuetype  {:name  "故障"}
                              :assignee {:displayName "赖sir"}
                              :customfield_10200 {:value "容易"}
                              :customfield_10300 {:value "一般错误"}
                              :status {:name "完成"}
                              :updated "2023-12-22T13:44:49.863+0800"}
                 }
             })
  (map (fn [activity] (-> activity
                          (select-keys [:assignnee :jira-key])
                          (assoc :title "hello"))) activities)
  (evaluate-jira-activity-score rules activities)

  (extract-necessary-jira-fields issue rules)

  )
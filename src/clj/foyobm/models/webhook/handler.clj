(ns foyobm.models.webhook.handler
  (:require [ring.util.response :as rr]
            [taoensso.timbre :as log]))

(defmulti handle-project-logic (fn [app data] app))

(defn extract-necessary-jira-fields
  "提取必要数据与字段
   1. 获取故障类型数据（非block,即无关联作为缺陷），状态为完成(非故事,缺陷类型)
   2. 必要字段：编号，类型，工时，关联任务，状态
  "
  [issue-data]
  (let [{:keys [issue]} issue-data
        {:keys [key fields]} issue
        {:keys [issuetype assignee status priority]} fields
        assign-name (-> assignee :displayName)
        status-name (-> status :name)
        priority-name (-> priority :name)
        activity (cond
                   (and (= status-name "完成") (some #{issuetype} #{"任务" "子任务" "优化"}))
                   (-> (log/info "记录研发任务活动数据")
                       {:issue-key key :issue-type issuetype :issue-assignee assign-name :name (format "完成任务[%s]累加积分" key) :priority-name priority-name})

                   (and (= status-name "完成") (= issuetype "故障"))
                   (-> (log/info "记录缺陷")
                       {:issue-key key :issue-type issuetype :issue-assignee assign-name :name (format "产生缺陷[%s]扣减积分" key) :priority-name priority-name})

                   :else nil)]
    (when activity
      (log/info activity))))

(defmethod handle-project-logic "jira" [app data]
  (log/info (str "ingest " app  " data: " (extract-necessary-jira-fields data)))
  (rr/response {:message "Jira logic processed"}))

(defmethod handle-project-logic "ding" [app data]
  (log/info "Processing Ding logic for ID:" app data)
  ;; 实现具体的逻辑
  (rr/response {:message "Ding logic processed"}))


(defmethod handle-project-logic :default [app data]
  (log/info "Processing Default logic for ID:" app)
  ;; 实现默认的逻辑
  (rr/response {:message "Default logic processed"}))


(defn handler-webhook-ingest [{:keys [env parameters]}]
  (let [{:keys [_]} env
        data (:body parameters)
        app (get-in parameters [:path :app])]
    (log/info "request data: " data)
    (handle-project-logic app data)
    (if true
      (rr/response {:message "Ok"})
      (rr/response {:error "ingest-error"}))))

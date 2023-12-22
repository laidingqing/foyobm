(ns foyobm.models.webhook.handler
  (:require [ring.util.response :as rr]
            [taoensso.timbre :as log]))

(defmulti handle-project-logic (fn [app data] app))

(defn extract-necessary-fields
  "提取必要数据与字段，获取故障类型数据（非block,即无关联作为缺陷），状态为完成(非故事类型)，编号，类型，工时，关联任务，状态"
  [data]
  (let [{:keys [issue]} data
        {:keys [key fields]} issue
        {:keys [issuetype assignee worklog issuelinks status]} fields]
    {:issue_key key :issue_type (:name issuetype) :issue_assignee (:displayName assignee)}))

(defmethod handle-project-logic "jira" [app data]
  (log/info (str "ingest " app  " data: " (extract-necessary-fields data)))
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
    (handle-project-logic app data)
    (if true
      (rr/response {:message "Ok"})
      (rr/response {:error "ingest-error"}))))

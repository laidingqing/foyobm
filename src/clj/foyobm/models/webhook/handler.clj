(ns foyobm.models.webhook.handler
  (:require [ring.util.response :as rr]
            [taoensso.timbre :as log]
            [foyobm.models.project.db :as project.db]
            [foyobm.models.rule.db :as rule.db]
            [foyobm.models.webhook.core :refer [extract-necessary-jira-fields]]))

(defmulti handle-project-logic (fn [app data db] app))



(defmethod handle-project-logic "jira" [app data db]
  (let [jira (project.db/get-project-by-name db "jira")
        rules (when jira (rule.db/find-rules-with-props db {:has-many true} {:project_id (-> jira :id)}))]
    (log/info (str "ingest " app  " data: " (extract-necessary-jira-fields data rules)))
    (rr/response {:message "Jira logic processed" :rules rules})))

(defmethod handle-project-logic "ding" [app data]
  (log/info "Processing Ding logic for ID:" app data)
  ;; 实现具体的逻辑
  (rr/response {:message "Ding logic processed"}))


(defmethod handle-project-logic :default [app data db]
  (log/info "Processing Default logic for ID:" app)
  ;; 实现默认的逻辑
  (rr/response {:message "Default logic processed"}))


(defn handler-webhook-ingest [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)
        app (get-in parameters [:path :app])]
    ;; (log/info "request data: " data)
    (handle-project-logic app data db)))

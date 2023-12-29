(ns foyobm.models.webhook.handler
  (:require [ring.util.response :as rr]
            [taoensso.timbre :as log]
            [foyobm.models.project.db :as project.db]
            [foyobm.models.rule.db :as rule.db]
            [foyobm.models.activity.db :as activity.db]
            [foyobm.models.webhook.core :refer [extract-necessary-jira-fields]]
            [foyobm.models.users.db :as user.db]))

(defmulti handle-project-logic (fn [app data db] app))



(defmethod handle-project-logic "jira" [app data db]
  (log/info (format "ingest %s webhook" app))
  (let [{:keys [issue]} data
        {:keys [fields]} issue
        {:keys [status]} fields
        status-name (-> status :name)]
    (if (not= status-name "完成")
      (rr/response {:message "not processed"})
      (let [jira (project.db/get-project-by-name db "jira")
            project-id (-> jira :id)
            rules (when jira (rule.db/find-rules-with-props db {:has-many true} {:project_id project-id}))
            activities (extract-necessary-jira-fields data rules)
            activities (map (fn [activity] (let [assignee (-> activity :assignee)
                                                 user-id (-> (user.db/find-user-by-name db assignee) :id)
                                                 new-activity (-> activity
                                                                  (assoc :user_id user-id :project_id project-id)
                                                                  (dissoc :assignee))]
                                             new-activity)) activities)]
        (when activities (activity.db/batch-create-activities db activities))
        (rr/response {:message "Jira logic processed" :rules rules})))))

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

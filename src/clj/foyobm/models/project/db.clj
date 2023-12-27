(ns foyobm.models.project.db
  (:require [foyobm.utils.query :as q]
            [taoensso.timbre :as log]
            [clojure.string :as str]))


;; project
;; datalog => (:webhook :timer :regular)

(defn get-all-projects [db]
  (q/db-query! db {:select [:*]
                   :from [:projects]}))


(defn get-project-by-name [db name]
  (let [find-project-sql {:select [:*]
                          :from [:projects]
                          :where [:= :name (str/lower-case name)]}
        find-settings-sql {:select [:*]
                           :from [:project_settings]}
        project (q/db-query-one! db find-project-sql)
        settings (when project (q/db-query! db (assoc find-settings-sql :where [:= :project_id (-> project :id)])))]
    (when project
      (assoc project :settings settings))))

(defn create-project 
  [db projects]
  (q/db-query-one! db {:insert-into :projects
                       :values [projects]}))

;; project-settings

(defn get-settings-by-project [db project-id]
  (q/db-query! db {:select [:*]
                   :from [:project_settings]
                   :where [:= :project_id project-id]}))

(defn create-project-setting
  [db setting]
  (q/db-query-one! db {:insert-into :project_settings
                       :values [setting]}))



;; score-rules
;; {:name "缺陷" :title "产生缺陷" :threshold 0 :score -2}

(comment
  (require '[foyobm.services.config :refer [read-config]]
           '[integrant.core :as ig]
           '[foyobm.services.db])

  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))

  (def project-1 {:name "Jira" :datalog "webhook" :description "连接Jira采集工作数据"})
  (create-project db project-1)
  (get-all-projects db)

  (def project-setting-1 {:project_id 1 :datalog "webhook" :name "uri" :value "/api/webhooks/jira"})
  (create-project-setting db project-setting-1)
  (get-settings-by-project db 1)

  (get-project-by-name db "jira")
  )






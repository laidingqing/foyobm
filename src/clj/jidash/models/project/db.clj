(ns jidash.models.project.db
  (:require [jidash.utils.query :as q]
            [clojure.string :as str]))



;; project dictionary

(defn get-all-project-dcits [db query]
  (let [{:keys [classv]} query
        {:keys [limit offset]} (merge {:limit 10 :offset 0} (filter some? query))
        sql {:select [:*
                      [[:over [[:count :id]]] "total"]]
             :from [:project_dicts]}
        classv-clause [:= :classv classv]
        where-clause (cond-> [:and]
                       classv (conj classv-clause))
        sql (cond-> sql
              where-clause (assoc :where where-clause)
              limit (assoc :limit limit)
              offset (assoc :offset offset))]
    (q/db-query! db sql)))

(defn create-project-dict
  [db dict]
  (q/db-query-one! db {:insert-into :project_dicts
                       :values [dict]}))

;; project
;; datalog => (:webhook :timer :regular)

(defn get-all-projects [db query]
  (let [{:keys [limit offset company_id]} query]
    (q/db-query! db {:select [:*]
                     :from [:projects]
                     :where [:and [:= :company_id company_id] [:= :activated true]]
                     :limit limit
                     :offset offset})))


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


(defn find-project-by-name-and-company [db name company-id]
  (q/db-query-one! db {:select [:id]
                       :from [:projects]
                       :where [:and [:= :name name] [:= :company_id company-id]]}))


(defn create-project
  [db projects]
  (q/db-query-one! db {:insert-into :projects
                       :values [projects]}))

(defn update-project
  [db data id]
  (q/db-query-one! db {:update :projects
                       :set data
                       :where [:= :id id]}))

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
  (require '[jidash.services.config :refer [read-config]]
           '[integrant.core :as ig]
           '[jidash.services.db])

  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))

  (def project-1 {:name "Jira" :datalog "webhook" :description "连接Jira采集工作数据" :company_id 11})
  (create-project db project-1)
  (get-all-projects db {})

  (def project-setting-1 {:project_id 1 :datalog "webhook" :name "uri" :value "/api/webhooks/jira"})
  (create-project-setting db project-setting-1)
  (get-settings-by-project db 1)
  (get-all-project-dcits db {:limit 10 :offset 0})
  (get-project-by-name db "jira")
  )






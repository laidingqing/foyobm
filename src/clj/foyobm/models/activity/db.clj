(ns foyobm.models.activity.db
  (:require [foyobm.utils.query :as q]
            [taoensso.timbre :as log]))


;; user's score activity
;;    user_id bigint not null,
;;    score bigint not null default 0,
;;    title varchar(100) not null,
;;    project_id bigint not null,

(defn get-activity-list
  [db query params]
  (let [sql {:select [:*]
             :from [:activities]}
        {:keys [limit offset sort-field sort-dir]} (merge {:limit 10 :offset 0 :sort-dir "desc"} (filter some? params))
        {:keys [project-id user-id]} query
        project-clause [:= :project-id project-id]
        user-clause [:= :user-id user-id]
        where-clause (cond-> [:and]
                       project-id (conj project-clause)
                       user-id (conj user-clause))
        sql (cond-> sql
              where-clause (assoc :where where-clause)
              limit (assoc :limit limit)
              offset (assoc :offset offset)
              sort-field (assoc :order-by [[sort-field sort-dir]])
              )]
    
    (q/db-query! db sql)))

(defn create-activity
  [db activity]
  (q/db-query-one! db {:insert-into :activities
                       :values [activity]}))

(comment
  (require '[foyobm.services.config :refer [read-config]]
           '[integrant.core :as ig]
           '[foyobm.services.db])

  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))


  (def activity-1 {:user_id 1 :score 100 :title "提早完成任务[CKMRO-456]" :project_id 1})
  (create-activity db activity-1)

  (get-activity-list db {:project-id 1 :user-id 1} {:sort-field "created" :sort-dir "desc"})
  )
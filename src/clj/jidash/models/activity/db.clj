(ns jidash.models.activity.db
  (:require [jidash.utils.query :as q]
            [taoensso.timbre :as log]))

(defn find-activity-list
  [db query]
  (let [sql {:select [:*
                      [[:over [[:count :id]]] "total"]]
             :from [:activities]}
        {:keys [limit offset sort-field sort-dir]} (merge {:limit 10 :offset 0 :sort-dir "desc" :sort-field :created} (filter some? query))
        {:keys [user_id]} query
        user-clause [:= :user_id user_id]
        where-clause (cond-> [:and]
                       user_id (conj user-clause))
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

(defn batch-create-activities
  [db activities]
  (q/db-query-one! db {:insert-into :activities
                       :values activities}))

(comment
  (require '[jidash.services.config :refer [read-config]]
           '[integrant.core :as ig]
           '[jidash.services.db])

  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))


  (def activity-1 {:user_id 1 :score 100 :title "提早完成任务[CKMRO-456]" :project_id 1})
  (create-activity db activity-1)

  (find-activity-list db {:project-id 1 :user-id 1 :sort-field "created" :sort-dir "desc"})
  )
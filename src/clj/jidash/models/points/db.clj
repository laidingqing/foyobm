(ns jidash.models.points.db
  (:require [jidash.utils.query :as q]
            [taoensso.timbre :as log]))



(defn create-point
  "创建积分数据"
  [db point]
  (q/db-query-one! db {:insert-into :points
                       :values [point]}))

(defn query-user-point
  [db user-id]
  (let [sql {:select [:*]
             :from [:points]
             :where [:= :user_id user-id]}
        user-point (q/db-query-one! db sql)]
    (if user-point
      user-point
      (create-point db {:user_id user-id :points 0}))))


(defn update-user-point
  [db user-id data]
  (let [sql {:update :points
             :set data
             :where [:= :user_id user-id]}]
    (q/db-query-one! db sql)))


(defn query-point-list
  [db query]
  (let [sql {:select [:*]
             :from [:points]}
        {:keys [limit offset sort-field sort-dir]} (merge {:limit 10 :offset 0 :sort-dir "desc"} (filter some? query))
        {:keys [user-id]} query
        user-clause [:= :user-id user-id]
        where-clause (cond-> [:and]
                       user-id (conj user-clause))
        sql (cond-> sql
              where-clause (assoc :where where-clause)
              limit (assoc :limit limit)
              offset (assoc :offset offset)
              sort-field (assoc :order-by [[sort-field sort-dir]]))]
    
    (q/db-query! db sql)))


(comment
  
  (require '[jidash.services.config :refer [read-config]]
           '[integrant.core :as ig]
           '[jidash.services.db])
  
  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))
  
  
  (def point-1 {:user_id 1 :points 100})
  (create-point db point-1)


  (query-point-list db {:user-id 1 :limit 20})


  )
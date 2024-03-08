(ns jidash.models.okr.db
    (:require [jidash.utils.query :as q]
             [taoensso.timbre :as log]))



(defn create-objective
  [db objective]
  (q/db-query-one! db {:insert-into :okrs
                       :values [objective]}))

(defn find-objective-by-id [db id]
  (q/db-query-one! db {:select [:o.* :c.user_name]
                       :from [[:okrs :o]]
                       :where [:= :o.id id]
                       :join [[:users :c] [:= :c.id :o.user_id]]}))

(defn get-key-results-by-oid [db o-id]
  (q/db-query! db {:select [:o.*]
                       :from [[:key_results :o]]
                       :where [:= :o.o_id o-id]})
  )

;; 统一查询Objective列表.
(defn query-objective-list
  [db query]
  (let [sql {:select [:o.* :c.user_name
                      [[:over [[:count :o.id]]] "total"]]
             :from [[:okrs :o]]}
        {:keys [limit offset sort-field sort-dir]} (merge {:limit 10 :offset 0 :sort-dir "desc"} (filter some? query))
        {:keys [u_id]} query
        {:keys [c_id]} query
        user-clause [:= :user_id u_id]
        company-clause [:= :company_id c_id]
        where-clause (cond-> [:and]
                       u_id (conj user-clause)
                       c_id (conj company-clause))
        sql (cond-> sql
              where-clause (assoc :where where-clause)
              limit (assoc :limit limit)
              offset (assoc :offset offset)
              sort-field (assoc :order-by [[sort-field sort-dir]]))
        sql (assoc sql :join [[:users :c] [:= :c.id :o.user_id]])]
    (q/db-query! db sql)))


(defn create-key-results
  [db kr]
  (q/db-query-one! db {:insert-into :key_results
                       :values [kr]}))
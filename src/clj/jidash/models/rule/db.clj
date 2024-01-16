(ns jidash.models.rule.db
  (:require [jidash.utils.query :as q]
            [taoensso.timbre :as log]))

;; rule-props
;; rule_id bigint not null,
;; field varchar (100), -- priority/name, 没有二级默认取name字段
;; operator varchar (20) not null, -- [>, >= , <, <=, !=]
;; target varchar (100) not null,
;; score bigint not null default 0,

(defn create-rule-props
  [db rules]
  (q/db-query-one! db {:insert-into :rule_props
                         :values rules}))


(defn get-all-props-by-rule [db rule-id]
  (q/db-query! db {:select [:*]
                   :from [:rule_props]
                   :where [:= :rule_id rule-id]}))


;; rule
;; project_id bigint not null,
;; title varchar(50) not null,
;; status bigint not null default 1, -- 0禁用, 1启用
;; score bigint not null default 0, -- 默认积分值
;; event_key varchar(100) not null
(defn create-rule
  [db rule]
  (q/db-query-one! db {:insert-into :rules
                       :values [rule]}))

  
;; 这里写不好，应使用SQL JOIN完成一对多关系的结构重组 
;; TODO
(defn find-rules-with-props
  [db param query]
  (let [sql {:select [:*] :from [:rules]}
        {:keys [has-many]} param
        {:keys [project-id]} query
        where-clause (cond-> [:and]
                       project-id (conj [:= :project-id project-id]))
        sql (cond-> sql
              project-id (assoc :where where-clause))
        query-result (q/db-query! db sql)
        query-result (when has-many  (map #(assoc % :props (get-all-props-by-rule db (:id %))) query-result))]
    query-result))

(defn find-rule-with-props
  [db rule-id]
  (let [rule (q/db-query-one! db {:select [:*]
                              :from [:rules]
                              :where [:= :id rule-id]})
        props (when rule (get-all-props-by-rule db rule-id))]
    (when props
      (assoc rule :props props))))


(comment
  (require '[jidash.services.config :refer [read-config]]
           '[integrant.core :as ig]
           '[jidash.services.db])

  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))

  (def rule-1 {:project_id 1 :title "完成任务" :status 1 :score 10 :event_key "finish"})
  (create-rule db rule-1)


  (def rule-props-1 [{:rule_id 1 :field "priority" :operator "=" :target "Medium" :score 20}
                     {:rule_id 1 :field "priority" :operator "=" :target "Low" :score 10}] )

  (create-rule-props db rule-props-1)
  (get-all-props-by-rule db 1)
  (find-rule-with-props db 1)
  (find-rules-with-props db {:has-many true} {:project-id 1})
  )
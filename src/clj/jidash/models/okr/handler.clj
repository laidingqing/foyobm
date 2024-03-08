(ns jidash.models.okr.handler
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [jidash.models.spec :as spec]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]
            [jidash.models.okr.db :as okr.db]
            [jidash.utils.datetime :refer [extract-date-from-string]]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(defn datetime-to-timestamp [datetime]
  (let [millis (.getMillis datetime)
        timestamp (java.sql.Timestamp. millis)]
    timestamp)) 


;; 快速创建Objective
;; 推进创建职业化研发团队[[2024/03/01]] 解构标题中的日期为目标开始周期月份
(defn create-simply-okr
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)
        ext-value (extract-date-from-string (:title data)) 
        valid? (s/valid? ::spec/create-objective data) 
        objective (when valid?
                    (let [first-day (t/date-time (:year ext-value) (:month ext-value) 1)
                          last-day (-> first-day
                                       (t/plus (t/months 1))
                                       (t/minus (t/days 1)))
                          new-value (merge data {:title (:title ext-value)
                                                 :started (datetime-to-timestamp first-day)
                                                 :ended (datetime-to-timestamp  last-day)})]
                      (okr.db/create-objective db new-value)
                      ))]
    (if objective
      (rr/response objective)
      (rr/response {:msg "create objective error"}))))


(defn handle-query-objectives
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        query-params (get-in parameters [:query])
        objectives (okr.db/query-objective-list db query-params)
        results (map #(assoc % :key_results (okr.db/get-key-results-by-oid db (:id %))) objectives)]
    (if results
      (rr/response results)
      (rr/response {:error "query objectives error."}))))


(defn handle-get-objective
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        objective-id (get-in parameters [:path :id])
        objective (okr.db/find-objective-by-id db objective-id)]
    (if objective
      (rr/response objective)
      (rr/response {:error "query objective error."}))))



(defn create-key-results
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)
        objective-id (get-in parameters [:path :id])
        objective (okr.db/find-objective-by-id db objective-id)
        valid? (s/valid? ::spec/create-key-result data)]
    (if (and valid? objective)
      (let [kr (okr.db/create-key-results db data)]
        (if kr
          (rr/response kr)
          (rr/response {:error "create k-r error."})))
      (rr/response {:error "create k-r error."}))))
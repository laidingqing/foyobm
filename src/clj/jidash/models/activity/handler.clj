(ns jidash.models.activity.handler
    (:require [clojure.spec.alpha :as s]
              [next.jdbc :as jdbc]
              [jidash.models.spec :as spec]
              [ring.util.response :as rr]
              [taoensso.timbre :as log]
              [jidash.models.activity.db :as activity.db]
              [jidash.models.points.db :as point.db]
              [jidash.models.users.db :as user.db]
              [clojure.data.csv :as csv]
              [clojure.java.io :as io]
              [clj-bom.core :as bom]))

(defn handle-query-activities
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        query-params (get-in parameters [:query])
        activities (activity.db/find-activity-list db query-params)]
    (if activities
      (rr/response activities)
      (rr/response {:error "query activities error."}))))



(defn- parse-batch-data->event
  [{:keys [title data catalog]}])


(defn- process-user-point
  [db db-user point title company-id]
  (jdbc/with-transaction [tx db]
    (let [user_name (:user_name db-user)
          user-id (:id db-user)
          db-user-point (point.db/query-user-point tx user-id)
          _ (activity.db/create-activity tx {:user_id user-id :pre_score (:points db-user-point) :score point :title title :catalog "manual" :name user_name})

          last-point (+ (:points db-user-point) point)
          db-point (point.db/update-user-point tx user-id {:user_id user-id :company_id company-id :points last-point})]
      db-point)))


(defn handle-batch-create-activity
  "批量处理积分"
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        {{:keys [file]} :multipart} parameters]
    (let [rows (csv/read-csv file)]
      (println rows))

    (rr/response {:msg "Ok."}))
  
)


(defn handle-create-activity
  "create department info with manager."
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)
        title (:title data)
        catalog (:catalog data)
        user-id (:user_id data)
        company-id (:company_id data)
        point (or (:score data) 0)
        db-user (user.db/find-user-by-id db user-id)]

    (if (and db-user (= catalog "manual"))
      (let [db-point (process-user-point db db-user point title company-id)]
        (if db-point
          (rr/response {:id (:id db-point)})
          (rr/response {:error "create-activity error"})))
      (rr/response {:error "not-allowed "})))
  )
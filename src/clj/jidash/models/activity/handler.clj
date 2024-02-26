(ns jidash.models.activity.handler
    (:require [clojure.spec.alpha :as s]
              [next.jdbc :as jdbc]
              [jidash.models.spec :as spec]
              [ring.util.response :as rr]
              [taoensso.timbre :as log]
              [jidash.models.activity.db :as activity.db]
              [jidash.models.points.db :as point.db]
              [jidash.models.users.db :as user.db]))





(defn handle-query-activities
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        query-params (get-in parameters [:query])
        activities (activity.db/find-activity-list db query-params)]
    (if activities
      (rr/response activities)
      (rr/response {:error "query activities error."}))))



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
      (jdbc/with-transaction [tx db]
        (let [user_name (:user_name db-user)
              _ (activity.db/create-activity tx {:user_id user-id :score point :title title :catalog "manual" :name user_name})
              db-user-point (point.db/query-user-point tx user-id)
              last-point (+ (:points db-user-point) point)
              db-point (point.db/update-user-point tx user-id {:user_id user-id :company_id company-id :points last-point})]
          (if db-point
            (rr/response {:id (:id db-point)})
            (rr/response {:error "create-activity error"}))))
      (rr/response {:error "not-allowed "}))))
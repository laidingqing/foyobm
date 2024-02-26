(ns jidash.models.points.handler
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [jidash.models.spec :as spec]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]
            [jidash.models.points.db :as points.db]))




(defn handle-query-points
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        query-params (get-in parameters [:query])
        points (points.db/query-point-list db query-params)]
    (if points
      (rr/response points)
      (rr/response {:error "query points error."}))))
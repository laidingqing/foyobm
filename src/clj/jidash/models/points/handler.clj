(ns jidash.models.points.handler
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [jidash.models.spec :as spec]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]
            [jidash.models.points.db :as points.db]
            [clojure.data.csv :as csv]
            
            [clojure.java.io :as io]))




(defn handle-query-points
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        query-params (get-in parameters [:query])
        points (points.db/query-point-list db query-params)]
    (if points
      (rr/response points)
      (rr/response {:error "query points error."}))))


(defn handle-summary-user-points
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        {:keys [user_id year month day]} (get-in parameters [:query])
        y-sum (when year
                (points.db/summary-user-point-by-year db user_id year))
        m-sum (when month
                (points.db/summary-user-point-by-month db user_id month))
        d-sum (when day
                (points.db/summary-user-point-by-day db user_id day))
        points (merge [] y-sum m-sum d-sum)]
    (if points
      (rr/response (filter #(not (nil? %)) points))
      (rr/response {:error "query points error."}))))

(defn parse-batch-csv-point []
  
  )



(comment
  (:require  '[clojure.java.io :as io])
  
  (with-open [in-file (io/reader "resources/point.example.csv")]
    (doall
     (let [rows (csv/read-csv in-file)]
       (println rows)
       )
     )
    )

  )
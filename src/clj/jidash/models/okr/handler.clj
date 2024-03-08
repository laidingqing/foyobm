(ns jidash.models.okr.handler
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [jidash.models.spec :as spec]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]
            [jidash.models.points.db :as points.db]))




(defn create-simply-okr
  [{:keys [env parameters]}]
  (let [{:keys [db]} env]
    (rr/response {:id 1 :title "simple okr"})))
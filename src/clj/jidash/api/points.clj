(ns jidash.api.points
    (:require [jidash.models.points.handler :refer [handle-query-points]]
              [jidash.router.middleware :refer [wrap-authorization]]
              [jidash.models.spec :as spec]
              [clojure.spec.alpha :as s]))



(def point-routes
  ["/points"
   ["" {:get {:middleware [wrap-authorization]
              :parameters {:query {:c_id int? :limit int? :offset int?}}
              :handler handle-query-points}}]
   ["/user" {:get {:middleware [wrap-authorization]
              :parameters {:query {:c_id int? :user_id int? :limit int? :offset int?}}
              :handler handle-query-points}}]])
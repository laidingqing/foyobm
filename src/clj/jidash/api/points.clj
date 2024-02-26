(ns jidash.api.points
    (:require [jidash.models.points.handler :refer [handle-query-points]]
             [jidash.router.middleware :refer [wrap-authorization]]
             [jidash.models.spec :as spec]))



(def point-routes
  ["/points"
   ["" {:get {:middleware [wrap-authorization]
              :handler handle-query-points}}]])
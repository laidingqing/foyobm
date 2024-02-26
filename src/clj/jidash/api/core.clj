(ns jidash.api.core
  (:require [jidash.api.users :refer [user-routes]]
            [jidash.api.commons :refer [commons-route]]
            [jidash.api.points :refer [point-routes]]
            [jidash.api.activity :refer [activity-routes]]))


(def health-route
  ["/health-check"
   {:get (fn [_]
           {:status 200
            :body {:ping "pong"}})}])

(def routes
  [["/api"
    health-route
    user-routes
    commons-route
    point-routes
    activity-routes]])
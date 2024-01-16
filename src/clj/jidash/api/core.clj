(ns jidash.api.core
  (:require [jidash.api.users :refer [user-routes]]
            [jidash.api.basis :refer [basis-route]]
            [jidash.api.webhook :refer [webhook-routes]]
            [jidash.api.project :refer [project-route]]))


(def health-route
  ["/health-check"
   {:get (fn [_]
           {:status 200
            :body {:ping "pong"}})}])

(def routes
  [["/api"
    health-route
    user-routes
    basis-route
    project-route
    webhook-routes]])
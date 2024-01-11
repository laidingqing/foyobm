(ns foyobm.api.core
  (:require [foyobm.api.users :refer [user-routes]]
            [foyobm.api.basis :refer [basis-route]]
            [foyobm.api.webhook :refer [webhook-routes]]
            [foyobm.api.project :refer [project-route]]))


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
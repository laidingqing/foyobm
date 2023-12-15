(ns foyobm.api.core
  (:require [foyobm.api.users :refer [user-routes]]))


(def health-route
  ["/health-check"
   {:get (fn [_]
           {:status 200
            :body {:ping "pong"}})}])

(def routes
  [["/api"
    health-route
    user-routes]])
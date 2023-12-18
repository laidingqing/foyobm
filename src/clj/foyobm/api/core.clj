(ns foyobm.api.core
  (:require [foyobm.api.users :refer [user-routes]]
            [foyobm.api.company :refer [company-route]]))


(def health-route
  ["/health-check"
   {:get (fn [_]
           {:status 200
            :body {:ping "pong"}})}])

(def routes
  [["/api"
    health-route
    user-routes
    company-route]])
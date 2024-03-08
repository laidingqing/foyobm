(ns jidash.api.core
  (:require [jidash.api.users :refer [user-routes]]
            [jidash.api.commons :refer [commons-route]]
            [jidash.api.points :refer [point-routes]]
            [jidash.api.activity :refer [activity-routes]]
            [jidash.api.okr :refer [okr-routes]]
            [jidash.plugins.jira.core :refer [->JiraPointSource]]
            [jidash.plugins.core :as plugins]))


(def health-route
  ["/health-check"
   {:get (fn [_]
           {:status 200
            :body {:ping "pong"}})}])

(def jira-point-source (->JiraPointSource))

(def routes
  [["/api"
    health-route
    user-routes
    commons-route
    point-routes
    activity-routes
    okr-routes
    (plugins/handler jira-point-source)]])
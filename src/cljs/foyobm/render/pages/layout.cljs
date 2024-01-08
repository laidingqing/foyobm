(ns foyobm.render.pages.layout
  (:require [foyobm.db.auth :as auth]
            [foyobm.db.router :as router]
            [re-frame.core :as rf]
            [foyobm.render.pages.container.views :refer [authenticated-page-container generic-page-container]]))



(defn authenticated-page []
  (let [current-route @(rf/subscribe [::router/current-route])]
    [authenticated-page-container
     (when current-route
       [(-> current-route :data :view)])]))


(defn no-authenticated-page []
  (let [current-route @(rf/subscribe [::router/current-route])]
    [generic-page-container
     (when current-route
       [(-> current-route :data :view)])])
  )


(defn layout []
  (case @(rf/subscribe [::auth/signin-state])
    :signed-in [authenticated-page]
    [no-authenticated-page]))
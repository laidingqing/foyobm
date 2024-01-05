(ns foyobm.render.pages.layout
  (:require [foyobm.db.ui :as ui]
            [foyobm.db.auth :as auth]
            [foyobm.render.pages.views :as views]
            [re-frame.core :as rf]
            [foyobm.render.pages.container.views :refer [authenticated-page-container generic-page-container]]))



(defn authenticated-page []
  (let [page-key @(rf/subscribe [::ui/active-page])
        page-component (views/page-view page-key)]
    [authenticated-page-container
     [page-component]]))


(defn no-authenticated-page []
  (let [page-key @(rf/subscribe [::ui/active-page])]
    [generic-page-container
     (case page-key
       :login [(views/page-view :login)]
       [:div "Page not found"])])
  )


(defn layout []
  (case @(rf/subscribe [::auth/signin-state])
    :signed-in [authenticated-page]
    [no-authenticated-page]))
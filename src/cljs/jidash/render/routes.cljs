(ns jidash.render.routes
  (:require [reagent.dom]
            [re-frame.core :as rf]
            [reitit.frontend :as reitit]
            [jidash.db.common :as common]
            [jidash.db.points :as points]
            [jidash.render.pages.home.page :refer [home-page]]
            [jidash.render.pages.auth.login :refer [login-page]]
            [jidash.render.pages.auth.register :refer [register-page]]
            [jidash.render.pages.points.points :refer [point-list-page]]
            [jidash.render.pages.dashboard.page :refer [dash-page]]
            [jidash.render.pages.common.company :refer [company-edit-page]]
            [jidash.render.pages.common.users :refer [user-list-page]]
            [jidash.render.pages.common.groups :refer [groups-page]]))


(def routes
  ["/"
   ["" {:name ::home
        :view home-page
        :controllers
        []}]
   ["dashboard" {:name ::dashboard
                 :view dash-page
                 :controllers
                 []}]
   ["login" {:name ::login
             :view login-page
             :controllers
             []}]
   ["register" {:name ::register
                :view register-page
                :controllers
                []}]
   ["point-list" {:name ::point-list
                :view point-list-page
                :controllers  
                [{:start (fn []
                           (rf/dispatch [::common/fetch-members])
                           (rf/dispatch [::points/fetch-user-points]))}]}]
   ["settings/"
    ["company-edit" {:name ::company-edit
                     :view company-edit-page
                     :controllers
                     []}]
    ["user-list" {:name ::user-list
                  :view user-list-page
                  :controllers
                  [{:start (fn []
                             (rf/dispatch [::common/fetch-members]))}]}]
    ["group-list" {:name ::group-list
                   :view groups-page
                   :controllers
                   [{:start (fn []
                              (rf/dispatch [::common/fetch-groups]))}]}]]])

(def routing
  (reitit/router routes))
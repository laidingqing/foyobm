(ns jidash.render.routes
  (:require [reagent.dom]
            [re-frame.core :as rf]
            [reitit.frontend :as reitit]
            [jidash.db.basis :as basis]
            [jidash.db.settings :as settings]
            [jidash.render.pages.home.page :refer [home-page]]
            [jidash.render.pages.auth.login :refer [login-page]]
            [jidash.render.pages.auth.register :refer [register-page]]
            [jidash.render.pages.dashboard.page :refer [dash-page]]
            [jidash.render.pages.basis.company :refer [company-edit-page]]
            [jidash.render.pages.basis.users :refer [user-list-page]]
            [jidash.render.pages.basis.groups :refer [groups-page]]
            [jidash.render.pages.settings.project :refer [project-list-page]]
            [jidash.render.pages.settings.rules :refer [rule-list-page]]))


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
   ["basis/"
    ["company-edit" {:name ::company-edit
                     :view company-edit-page
                     :controllers
                     []}]
    ["user-list" {:name ::user-list
                  :view user-list-page
                  :controllers
                  [{:start (fn []
                             (rf/dispatch [::basis/fetch-members]))}]}]
    ["group-list" {:name ::group-list
                   :view groups-page
                   :controllers
                   [{:start (fn []
                              (rf/dispatch [::basis/fetch-groups]))}]}]]
   ["settings/"
    ["project-list" {:name ::project-list
                     :view project-list-page
                     :controllers
                     [{:start (fn []
                                (rf/dispatch [::settings/fetch-default-apps])
                                (rf/dispatch [::settings/fetch-my-apps]))}]}]
    ["rule-list" {:name ::rule-list
                  :view rule-list-page
                  :controllers
                  [{:start (fn []
                             (rf/dispatch [::settings/fetch-my-apps]))}]}]]])

(def routing
  (reitit/router routes))
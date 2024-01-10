(ns foyobm.render.routes
  (:require [reagent.dom]
            [re-frame.core :as rf]
            [reitit.frontend :as reitit]
            [foyobm.db.basis :as basis]
            [foyobm.render.pages.home.page :refer [home-page]]
            [foyobm.render.pages.auth.login :refer [login-page]]
            [foyobm.render.pages.auth.register :refer [register-page]]
            [foyobm.render.pages.dashboard.page :refer [dash-page]]
            [foyobm.render.pages.basis.company :refer [company-edit-page]]
            [foyobm.render.pages.basis.users :refer [user-list-page]]
            [foyobm.render.pages.basis.new-user :refer [new-user-page]]
            [foyobm.render.pages.basis.groups :refer [groups-page]]))


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
    ["new-user" {:name ::new-user
                 :view new-user-page
                 :controllers
                 []}]
    ["group-list" {:name ::group-list
                   :view groups-page
                   :controllers
                   [{:start (fn []
                              (rf/dispatch [::basis/fetch-groups]))}]}]]

   ])


(def routing
  (reitit/router routes))
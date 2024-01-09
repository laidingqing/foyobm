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
            [foyobm.render.pages.basis.users :refer [user-list-page]]))


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
                     [{:start (fn []
                                (rf/dispatch [::basis/fetch-current]))}]}]
    ["user-list" {:name ::user-list
                  :view user-list-page
                  :controllers
                  []}]]

   ])


(def routing
  (reitit/router routes))
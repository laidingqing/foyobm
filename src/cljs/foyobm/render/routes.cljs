(ns foyobm.render.routes
  (:require [reagent.dom]
            [re-frame.core :as rf]
            [reitit.frontend :as reitit]
            [foyobm.render.pages.home.page :refer [home-page]]
            [foyobm.render.pages.auth.login :refer [login-page]]
            [foyobm.render.pages.auth.register :refer [register-page]]))


(def routes
  ["/"
   ["" {:name ::home
        :view home-page
        :controllers
        []}]
   ["login" {:name ::login
             :view login-page
             :controllers
             []}]
   ["register" {:name ::register
                :view register-page
                :controllers
                []}]])


(def routing
  (reitit/router routes))
(ns foyobm.render.pages.views
  (:require [foyobm.render.pages.home.page :refer [home-page]]
            [foyobm.render.pages.login.page :refer [login-page]]))


(defn page-view  [page-name]
  (case page-name
    :home #'home-page
    :login #'login-page
    [home-page]))
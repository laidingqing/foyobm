(ns foyobm.render.pages.views
  (:require [foyobm.render.pages.home.page :refer [home-page]]
            [foyobm.render.pages.auth.login :refer [login-page]]
            [foyobm.render.pages.auth.register :refer [register-page]]))



(defn page-view  [page-name]
  (js/console.log "page-name: " page-name)
  (case page-name
    :home #'home-page
    :login #'login-page
    :register #'register-page
    [home-page]))
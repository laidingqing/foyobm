(ns foyobm.render.pages.views
  (:require [foyobm.render.pages.home.page :as home]
            [foyobm.render.pages.login.page :as login]
            [foyobm.render.pages.admin.page :as admin]))



(defn pages
  [page-name]
  (case page-name
    :home #'home/page
    :login #'login/page
    :admin #'admin/page))
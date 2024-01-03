(ns foyobm.render.pages.user.page
  (:require [re-frame.core :as rf]
            [foyobm.db.ui :as ui]
            [foyobm.render.utils :refer [render-main]]
            [foyobm.render.pages.user.usermanage :refer [user-manage-main]]))


(defn user-main []
  [:div "user manage"])


(defn pages
  [page-name]
  
  (case page-name
    :main [user-main]
    :user-manage [user-manage-main]))


(defn page []
  [:p "user..."])
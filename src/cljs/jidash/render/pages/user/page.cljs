(ns jidash.render.pages.user.page
  (:require [re-frame.core :as rf]
            [jidash.db.ui :as ui]
            [jidash.render.utils :refer [render-main]]
            [jidash.render.pages.user.usermanage :refer [user-manage-main]]))


(defn user-main []
  [:div "user manage"])


(defn pages
  [page-name]
  
  (case page-name
    :main [user-main]
    :user-manage [user-manage-main]))


(defn page []
  [:p "user..."])
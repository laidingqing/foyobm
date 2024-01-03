(ns foyobm.render.pages.layout
  (:require [foyobm.db.ui :as ui]
            [foyobm.db.auth :as auth]
            [foyobm.render.pages.views :as views]
            [re-frame.core :as rf]
            [foyobm.render.pages.container.views :refer [authenticated-page-container]]))



(defn layout []
  (let [active-page @(rf/subscribe [::ui/active-page])
        login? @(rf/subscribe [::auth/account])
        page-component (views/pages active-page)]
    [authenticated-page-container
     [page-component]])
  )
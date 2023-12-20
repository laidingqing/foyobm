(ns foyobm.render.pages.layout
  (:require [foyobm.render.components.header :refer [header-view]]
            [foyobm.db.ui :as ui]
            [foyobm.db.auth :as auth]
            [foyobm.render.pages.views :as views]
            [foyobm.render.components.dialogs :refer [dialog]]
            [re-frame.core :as rf]))


(defn layout []
  (let [active-page @(rf/subscribe [::ui/active-page])
        login? @(rf/subscribe [::auth/account])
        page-component (views/pages active-page)]
    [:div
     [header-view]
     [:div {:class "mx-auto"}
      [page-component]]
     [dialog]]
    )
  )
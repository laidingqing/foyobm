(ns foyobm.render.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [foyobm.render.pages.layout :refer [layout]]
   [foyobm.db.core :as events]))



(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/render [layout] root-el)))


(defn ^:export init []
  (js/console.log "application starting")
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
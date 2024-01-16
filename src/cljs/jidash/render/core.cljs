(ns jidash.render.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [jidash.render.pages.layout :refer [layout]]
   [jidash.db.core :as events]
   [jidash.render.routes :refer [routing]]
   [jidash.render.utils :refer [init-routes!]]))



(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (init-routes! routing)
    (rdom/render [layout] root-el)))


(defn ^:export init []
  (js/console.log "application starting") 
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
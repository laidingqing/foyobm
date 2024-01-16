(ns jidash.render.utils
  (:require [reitit.frontend.easy :as rfe]
            [re-frame.core :as rf]
            [jidash.db.router :as router]))


(defn render-children [children]
  (into [:<>] children))


(defn href
  ([k] (href k nil nil))
  ([k params] (href k params nil))
  ([k params query]
   (rfe/href k params query)))

(defn on-navigate [new-match]
  (when new-match
    (rf/dispatch [::router/navigated new-match])))

(defn init-routes! [router]
  (js/console.log "initializing routes")
  (rfe/start!
   router
   on-navigate
   {:use-fragment false}))

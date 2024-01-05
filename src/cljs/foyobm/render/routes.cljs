(ns foyobm.render.routes
  (:require [reagent.dom]
            [re-frame.core :as rf]
            [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [foyobm.db.ui :as ui]))

(def routes ["/" {""           :home
                  "login"      :login
                  "register"   :register}])


(def history
  (let [dispatch #(rf/dispatch [::ui/set-active-page {:page (:handler %)}])
        match #(bidi/match-route routes %)]
    (pushy/pushy dispatch match)))

(defn init! []
  (pushy/start! history))
(ns foyobm.db.settings
  (:require [re-frame.core :as rf]
            [foyobm.db.auth :as auth]
            [foyobm.db.ui :as ui]))

(def initial-state
  {::applications []})


;; fx

(rf/reg-event-fx
 ::fetch-default-apps
 (fn [{:keys [db]} [_]]
   {:fx [[:dispatch [:http {:url "/api/projects/applications"
                            :method :get
                            :on-success [::fetch-apps-success]
                            :on-failure [:http-failure]}]]]}))


(rf/reg-event-fx
 ::fetch-apps-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::applications] data))}))



;; subs

(rf/reg-sub
 ::applications
 (fn [db _]
   (get-in db [::applications])))
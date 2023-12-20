(ns foyobm.db.auth
  (:require [re-frame.core :as rf]
            [foyobm.db.ui :as ui]))

(def initial-state
  {::auth {}})


(rf/reg-sub
 ::account
 (fn [db]
   (get-in db [::auth :account])))

(rf/reg-event-fx
 ::login
 (fn [_ [_ data]]
   {:fx [[:dispatch [::ui/set-dialog :loading]]
         [:dispatch [:http {:url "/api/users/login"
                            :method :post
                            :data data
                            :on-success [::auth-success]
                            :on-failure [:http-failure]}]]]}))

(rf/reg-event-fx
 ::register
 (fn [_ [_ data]]
   {:fx [[:dispatch [::auth-success data]]]}))

(rf/reg-event-fx
 ::auth-success
 (fn [{:keys [db]} [_ data]]
   {:db (assoc-in db [::auth :account] data)
    :fx [[:dispatch [::ui/close-dialog]]
         [:set-local-storage [:account/token (:token data)]]
         [:dispatch [::ui/set-active-page {:page :home}]]]}))
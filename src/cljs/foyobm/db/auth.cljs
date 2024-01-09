(ns foyobm.db.auth
  (:require [re-frame.core :as rf]
            [foyobm.db.ui :as ui]
            [foyobm.db.router :as router]))

(def initial-state
  {::auth {}
   ::signin-state :signed-out})

;; subs

(rf/reg-sub
 ::account
 (fn [db]
   (get-in db [::auth :account])))


(rf/reg-sub
 ::signin-state
 (fn [db _]
   (get-in db [::signin-state])))



;; fx

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
 ::logout
 (fn [{:keys [db]}]
   {:db (assoc-in db [::signin-state] :signed-out)
    :fx [[:set-local-storage [:account/token (:token nil)]]]}))

(rf/reg-event-fx
 ::register
 (fn [_ [_ data]]
   {:fx [[:dispatch [::auth-success data]]]}))

(rf/reg-event-fx
 ::auth-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::auth :account] data)
            (assoc-in [::signin-state] :signed-in))
    :fx [[:dispatch [::ui/close-dialog]]
         [:set-local-storage [:account/token (:token data)]]
         [:dispatch [::router/push-state :foyobm.render.routes/dashboard]]]}))
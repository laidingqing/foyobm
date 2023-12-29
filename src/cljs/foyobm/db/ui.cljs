(ns foyobm.db.ui
  (:require [re-frame.core :as rf]))

(def initial-state
  {::dialog {:open? false
             :type :loading
             :error-message ""}
   ::active-page :home
   ::active-user-page :main})


(rf/reg-event-fx
 ::set-active-page
 (fn [{:keys [db]} [_ {:keys [page]}]]
   (let [set-page (assoc db ::active-page page)]
     (case page
       :home {:db set-page}
       (:login :register :admin-manage :user-manage) {:db set-page}))))


(rf/reg-sub
 ::active-page
 (fn [db _]
   (::active-page db))) 


;; user ui module
(rf/reg-event-fx
 ::set-active-use-page
 (fn [{:keys [db]} [_ {:keys [page]}]]
   (let [set-page (assoc db ::active-user-page page)]
     (case page
       :main {:db set-page}
       :user-manage {:db set-page}))))


(rf/reg-sub
 ::active-user-page
 (fn [db _]
   (::active-user-page db))) 



(rf/reg-event-db
 ::close-dialog
 (fn [db] (assoc-in db [::dialog :open?] false)))

(rf/reg-event-db
 ::set-dialog
 (fn [db [_ type error-message]]
   (assoc db ::dialog {:open? true
                       :type type
                       :error-message error-message})))

(rf/reg-sub
 ::dialog
 (fn [db]
   (get db ::dialog)))
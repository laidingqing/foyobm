(ns foyobm.db.ui
  (:require [re-frame.core :as rf]))

(def initial-state
  {::dialog {:open? false
             :type :loading
             :error-message ""}
   ::active-page :home})


(rf/reg-event-fx
 ::set-active-page
 (fn [{:keys [db]} [_ {:keys [page]}]]
   (let [set-page (assoc db ::active-page page)]
     (case page
       :home {:db set-page}
       (:login :register) {:db set-page}))))


(rf/reg-sub
 ::active-page
 (fn [db _]
   (::active-page db))) 
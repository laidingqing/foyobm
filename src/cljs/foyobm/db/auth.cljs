(ns foyobm.db.auth
  (:require [re-frame.core :as rf]))

(def initial-state
  {::auth {}})


(rf/reg-sub
 ::account
 (fn [db]
   (get-in db [::auth :account])))
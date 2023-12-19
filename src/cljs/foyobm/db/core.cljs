(ns foyobm.db.core
  (:require [foyobm.db.auth :as auth]
            [foyobm.db.ui :as ui]
            [re-frame.core :as rf]))


(def app-db
  (merge {}
         ui/initial-state
         auth/initial-state))

(rf/reg-event-db
 ::initialize-db
 (fn [] app-db))
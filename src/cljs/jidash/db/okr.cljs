(ns jidash.db.okr
  (:require [re-frame.core :as rf]
            [jidash.db.ui :as ui]
            [jidash.db.router :as router]))

(def initial-state
  {::okr-list {}})



(rf/reg-event-fx
 ::new-okr-form
 (fn [{:keys [db]} [_]]
   {:fx [[:dispatch [::ui/set-dialog :new-okr-form]]]}))
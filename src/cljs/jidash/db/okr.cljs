(ns jidash.db.okr
  (:require [re-frame.core :as rf]
            [jidash.db.ui :as ui]
            [jidash.db.auth :as auth]
            [jidash.db.router :as router]))

(def initial-state
  {::okr-list {}})



(rf/reg-event-fx
 ::new-okr-form
 (fn [{:keys [db]} [_]]
   {:fx [[:dispatch [::ui/set-dialog :new-okr-form]]]}))


;; create a okr simply.
(rf/reg-event-fx
 ::create-okr
 [(rf/inject-cofx :local-store)] 
 (fn [{:keys [local-store db]} [_ data]]
   (let [token (:store-token local-store)
         user-id (:store-uid local-store)
         company-id (:store-cid local-store)]
     {:fx [[:dispatch [::ui/set-dialog :loading]]
           [:dispatch [:http {:url (str "/api/okrs")
                              :method :post
                              :data data
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::create-okr-success]
                              :on-failure [:http-failure]}]]]})))

;; receive create okr response and dispatch something.
(rf/reg-event-fx
 ::create-okr-success
 (fn [{:keys [db]} [_ data]]
   (let [{:keys [id]} data]
     {:fx [[:dispatch [::ui/close-dialog]]
           [:dispatch [::router/push-state :jidash.render.routes/okr-detail {:id id}]]]})))
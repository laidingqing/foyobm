(ns jidash.db.okr
  (:require [re-frame.core :as rf]
            [jidash.db.ui :as ui]
            [jidash.db.router :as router]))

(def initial-state
  {::objectives {:data []
                 :pagination {:current 1
                              :pageSize 10}}

   ::objective {:data {}}})



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
         company-id (:store-cid local-store)
         data (merge data {:user_id (js/parseInt user-id)
                           :company_id (js/parseInt company-id)})]
     {:fx [[:dispatch [::ui/set-dialog :loading]]
           [:dispatch [:http {:url (str "/api/okrs")
                              :method :post
                              :data data
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::create-or-find-okr-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::get-okr
 [(rf/inject-cofx :local-store)]
 (fn [{:keys [local-store db]} [_ id]]
   (let [token (:store-token local-store)]
     {:fx [[:dispatch [::ui/set-dialog :loading]]
           [:dispatch [:http {:url (str "/api/okrs/" id)
                              :method :get
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::create-or-find-okr-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::list-cycle-okr
 [(rf/inject-cofx :local-store)]
 (fn [{:keys [local-store db]} [_ user_id]]
   (let [token (:store-token local-store)
         user-id (:store-uid local-store)
         company-id (:store-cid local-store)
         query {:u_id (or user_id user-id)
                :c_id company-id
                :limit 999
                :offset 0}]
     {:fx [
           [:dispatch [:http {:url (str "/api/okrs")
                              :method :get
                              :query query
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::list-cycle-okr-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::list-cycle-okr-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::objectives :data] data))
    :fx [[:dispatch [::ui/close-dialog]]]}))

;; receive create okr response and dispatch something.
(rf/reg-event-fx
 ::create-or-find-okr-success
 (fn [{:keys [db]} [_ data]]
   (let [{:keys [id]} data]
     {:db (-> db
              (assoc-in [::objective :data] data))
      :fx [[:dispatch [::ui/close-dialog]]
           [:dispatch [::list-cycle-okr]]]})))
;; sub

(rf/reg-sub
 ::objective
 (fn [db _]
   (get-in db [::objective :data])))

(rf/reg-sub
 ::objectives
 (fn [db _]
   (get-in db [::objectives :data])))

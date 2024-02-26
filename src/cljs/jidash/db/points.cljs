(ns jidash.db.points
  (:require [re-frame.core :as rf]
            [jidash.db.auth :as auth]
            [jidash.db.ui :as ui]
            [jidash.render.utils.conversion :refer [remove-nils]]))

(def initial-dict-form {})

(def initial-state
  {::user-activities {:data []
                 :pagination {:current 0
                              :pageSize 10}
                 :filters initial-dict-form}
   
   ::user-points {:data []
                  :pagination {:current 0
                               :pageSize 10}}})

(rf/reg-event-fx
 ::fetch-user-activities
 (fn [{:keys [db]} [_ user_id]]
   (let [token (get-in db [::auth/auth :account :token])
         query {:user_id user_id}]
     {:fx [[:dispatch [:http {:url (str "/api/activities")
                              :method :get
                              :query query
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::fetch-user-activities-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::fetch-user-activities-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::user-activities :data] data))}))

(rf/reg-event-fx
 ::fetch-user-points
 (fn [{:keys [db]} [_]]
   (let [token (get-in db [::auth/auth :account :token])
         company-id (get-in db [::auth/company :form :id])
         query {:c_id company-id}]
     {:fx [[:dispatch [:http {:url (str "/api/points")
                              :method :get
                              :query query
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::fetch-user-points-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::fetch-user-points-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::user-points :data] data))}))

(rf/reg-event-fx
 ::new-point-form
 (fn [{:keys [db]} [_]]
   {:fx [[:dispatch [::ui/set-dialog :new-point-form]]]}))


(rf/reg-event-fx
 ::create-user-point
 (fn [{:keys [db]} [_ data]]
   (let [token (get-in db [::auth/auth :account :token])
         company-id (get-in db [::auth/company :form :id])
         data (merge data {:company_id company-id})
         ]
     {:fx [[:dispatch [::ui/set-dialog :loading]]
           [:dispatch [:http {:url (str "/api/activities")
                              :method :post
                              :data data
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::create-user-point-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::create-user-point-success
 (fn [{:keys [db]} [_ data]]
   {:fx [[:dispatch [::ui/close-dialog]]
         [:dispatch [::fetch-user-points]]]}))

;; subs

(rf/reg-sub
 ::user-points
 (fn [db _]
   (get-in db [::user-points :data])))

(rf/reg-sub
 ::user-points-pagination
 (fn [db _]
   (get-in db [::user-points :pagination])))

(rf/reg-sub
 ::user-activities
 (fn [db _]
   (get-in db [::user-activities :data])))

(rf/reg-sub
 ::user-activities-pagination
 (fn [db _]
   (get-in db [::user-activities :pagination])))
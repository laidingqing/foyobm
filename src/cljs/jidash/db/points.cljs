(ns jidash.db.points
  (:require [re-frame.core :as rf]
            [jidash.db.auth :as auth]
            [jidash.db.ui :as ui]
            [jidash.render.utils.conversion :refer [remove-nils]]))

(def initial-dict-form {})

(def initial-state
  {::user-activities {:user_id nil
                      :data []
                      :pagination {:current 1
                                   :pageSize 10}
                      :filters initial-dict-form}
   
   ::user-points {:data []
                  :pagination {:current 1
                               :pageSize 10}}})

(rf/reg-event-fx
 ::fetch-user-activities
 [(rf/inject-cofx :local-store)] 
 (fn [{:keys [local-store db]} [_]]
   (let [token (:store-token local-store)
         db-user-id (:store-uid local-store)
         {:keys [user_id]} (get-in db [::user-activities])
         {:keys [current pageSize] } (get-in db [::user-activities :pagination])
         query {:user_id (or user_id db-user-id)
                :limit pageSize
                :offset (* pageSize (- current 1))}]
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
 [(rf/inject-cofx :local-store)] 
 (fn [{:keys [local-store db]} [_]]
   (let [token (:store-token local-store)
         {:keys [current pageSize]} (get-in db [::user-points :pagination])
         company-id (:store-cid local-store)
         query {:c_id company-id
                :limit pageSize
                :offset (* pageSize (- current 1))}]
     {:fx [[:dispatch [:http {:url (str "/api/points")
                              :method :get
                              :query query
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::fetch-user-points-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::fetch-user-point
 [(rf/inject-cofx :local-store)] 
 (fn [{:keys [local-store db]} [_]]
   (let [token (:store-token local-store)
         user-id (:store-uid local-store)
         company-id (:store-cid local-store)
         {:keys [current pageSize]} (get-in db [::user-points :pagination])
         query {:c_id company-id
                :user_id user-id
                :limit pageSize
                :offset (* pageSize (- current 1))}]
     {:fx [[:dispatch [:http {:url (str "/api/points/user")
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


(rf/reg-event-fx
 ::set-activity-page
 (fn [{:keys [db]} [_ n]]
   {:db (assoc-in db [::user-activities :pagination :current] n)
    :fx [[:dispatch [::fetch-user-activities]]]}))

(rf/reg-event-fx
 ::set-activity-user-id
 (fn [{:keys [db]} [_ n]]
   {:db (-> db
            (assoc-in [::user-activities :pagination :current] 1)
            (assoc-in [::user-activities :user_id] n))
    :fx [[:dispatch [::fetch-user-activities]]]}))

(rf/reg-event-fx
 ::set-point-page
 (fn [{:keys [db]} [_ n]]
   {:db (assoc-in db [::user-points :pagination :current] n)
    :fx [[:dispatch [::fetch-user-points]]]}))
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


(rf/reg-sub
 ::user-activities-user-id
 (fn [db _]
   (get-in db [::user-activities :user_id])))
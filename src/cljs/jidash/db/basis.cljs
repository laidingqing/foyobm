(ns jidash.db.basis
  (:require [re-frame.core :as rf]
            [jidash.db.auth :as auth]
            
            [jidash.db.ui :as ui]))

(def initial-state
  {::members {:data []
              :pagination {:current 1
                           :pageSize 10}}
   ::groups {:data []
             :pagination {:current 1
                          :pageSize 10}}}
)



;; fx

(rf/reg-event-fx
 ::fetch-members
 (fn [{:keys [db]} [_]]
   (let [token (get-in db [::auth/auth :account :token])
         company-id (get-in db [::auth/company :form :id])]
     {:fx [[:dispatch [:http {:url (str "/api/basis/companies/" company-id "/members")
                              :method :get
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::fetch-members-success]
                              :on-failure [:http-failure]}]]]})))


(rf/reg-event-fx
 ::fetch-members-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::members :data] data))}))



(rf/reg-event-fx
 ::fetch-groups
 (fn [{:keys [db]} [_]]
   (let [token (get-in db [::auth/auth :account :token])
         company-id (get-in db [::auth/company :form :id])]
     {:fx [[:dispatch [:http {:url (str "/api/basis/companies/" company-id "/departments")
                              :method :get
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::fetch-groups-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::create-group
 (fn [{:keys [db]} [_ data]]
   (let [token (get-in db [::auth/auth :account :token])
         company-id (get-in db [::auth/company :form :id])]
     {:fx [[:dispatch [::ui/set-dialog :loading]]
           [:dispatch [:http {:url (str "/api/basis/companies/" company-id "/departments")
                              :method :post
                              :data data
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::create-group-success]
                              :on-failure [:http-failure]}]]]})))
(rf/reg-event-fx
 ::create-group-success
 (fn [{:keys [db]} [_ data]]
   {:fx [[:dispatch [::ui/close-dialog]]
         [:dispatch [::fetch-groups]]]}))

(rf/reg-event-fx
 ::fetch-groups-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::groups :data] data))}))

;; subs

(rf/reg-sub
 ::members
 (fn [db _]
   (get-in db [::members :data])))

(rf/reg-sub
 ::members-pagination
 (fn [db _]
   (get-in db [::members :pagination])))

(rf/reg-sub
 ::groups
 (fn [db _]
   (get-in db [::groups :data])))

(rf/reg-sub
 ::groups-pagination
 (fn [db _]
   (get-in db [::groups :pagination])))
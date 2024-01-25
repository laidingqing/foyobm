(ns jidash.db.settings
  (:require [re-frame.core :as rf]
            [jidash.db.auth :as auth]
            [jidash.db.ui :as ui]))

(def initial-state
  {::applications []
   ::application {}
   ::project-dicts []
   ::my-apps {:data []
              :pagination {:current 1
                           :pageSize 10}}})


;; fx

(rf/reg-event-fx
 ::fetch-default-apps
 (fn [{:keys [db]} [_]]
   {:fx [[:dispatch [:http {:url "/api/projects/applications"
                            :method :get
                            :on-success [::fetch-apps-success]
                            :on-failure [:http-failure]}]]]}))

(rf/reg-event-fx
 ::fetch-my-apps
 (fn [{:keys [db]} [_]]
   (let [token (get-in db [::auth/auth :account :token])
         company_id (get-in db [::auth/company :form :id])
         query {:limit 100
                :offset 0
                :company_id company_id}]
     {:fx [[:dispatch [:http {:url "/api/projects"
                              :method :get
                              :query query
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::fetch-my-apps-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::fetch-project-dicts
 (fn [{:keys [db]} [_]]
   (let [token (get-in db [::auth/auth :account :token])
         query {}]
     {:fx [[:dispatch [:http {:url "/api/projects/dicts"
                              :method :get
                              :query query
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::fetch-project-dicts-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::create-project
 (fn [{:keys [db]} [_ data]]
   (let [token (get-in db [::auth/auth :account :token])
         company_id (get-in db [::auth/company :form :id])
         data (merge data {:company_id company_id})]
     {:fx [[:dispatch [:http {:url "/api/projects"
                              :method :post
                              :data data
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::create-project-success]
                              :on-failure [:http-failure]}]]]})))


(rf/reg-event-fx
 ::create-project-success
 (fn [{:keys [db]} [_ data]]
   {:fx [[:dispatch [::fetch-my-apps]]]}))

(rf/reg-event-fx
 ::fetch-apps-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::applications] data))}))


(rf/reg-event-fx
 ::fetch-project-dicts-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::project-dicts] data))}))


(rf/reg-event-fx
 ::fetch-my-apps-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::my-apps :data] data))}))




(rf/reg-event-fx
 ::select-application
 (fn [{:keys [db]} [_ data]]
   {:db (assoc-in db [::application] data)
    :fx []}))



(rf/reg-event-fx
 ::open-rule-form
 (fn [{:keys [db]} [_]]
   {:fx [[:dispatch [::ui/set-dialog :rule-form]]]}))

;; subs

(rf/reg-sub
 ::project-dicts
 (fn [db _]
   (get db ::project-dicts)))

(rf/reg-sub
 ::applications
 (fn [db _]
   (get db ::applications)))

(rf/reg-sub
 ::application
 (fn [db _]
   (get db ::application)))

(rf/reg-sub
 ::my-apps
 (fn [db _]
   (get-in db [::my-apps :data])))

(rf/reg-sub
 ::apps-pagination
 (fn [db _]
   (get-in db [::my-apps :pagination])))


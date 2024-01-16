(ns foyobm.db.settings
  (:require [re-frame.core :as rf]
            [foyobm.db.auth :as auth]
            [foyobm.db.ui :as ui]))

(def initial-state
  {::applications []
   ::application {}
   ::my-apps []})


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
 ::fetch-my-apps-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::my-apps] data))}))




(rf/reg-event-fx
 ::select-application
 (fn [{:keys [db]} [_ data]]
   {:db (assoc-in db [::application] data)
    :fx []}))

;; subs

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
   (get db ::my-apps)))
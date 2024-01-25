(ns jidash.db.settings
  (:require [re-frame.core :as rf]
            [jidash.db.auth :as auth]
            [jidash.db.ui :as ui]
            [jidash.render.utils.conversion :refer [remove-nils]]))

(def initial-dict-form {:classv nil
                        })

(def initial-state
  {::applications []
   ::application {}
   ::project-dicts {:data []
                    :pagination {:current 0
                                 :pageSize 10}
                    :filters initial-dict-form}
   ::my-apps {:data []
              :pagination {:current 0
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
   (let [{:keys [current
                 pageSize]} (get-in db [::my-apps :pagination])
         token (get-in db [::auth/auth :account :token])
         company_id (get-in db [::auth/company :form :id])
         query {:limit pageSize
                :offset (* pageSize (- current 1))
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
   (let [{:keys [current
                 pageSize]} (get-in db [::project-dicts :pagination])
         token (get-in db [::auth/auth :account :token])
         filters (-> db
                     (get-in [::project-dicts :filters])
                     (remove-nils))
         query (merge {:limit pageSize
                       :offset (* pageSize (- current 1))} filters)]
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
            (assoc-in [::project-dicts :data] data))}))


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


(rf/reg-event-fx
 ::new-project-dict-form
 (fn [{:keys [db]} [_]]
   {:fx [[:dispatch [::ui/set-dialog :new-project-dict-form]]]}))

(rf/reg-event-fx
 ::set-dicts-curr-page
 (fn [{:keys [db]} [_ n]]
   {:db (-> db
            (assoc-in [::project-dicts :pagination :current] n))
    :fx [[:dispatch [::fetch-project-dicts]]]}))


(rf/reg-event-db
 ::set-dict-filter-value
 (fn [db [_ attr v]]
   (assoc-in db [::project-dicts :filters attr] v)))

;; subs

(rf/reg-sub
 ::project-dicts
 (fn [db _]
   (get-in db [::project-dicts :data])))

(rf/reg-sub
 ::project-dicts-pagination
 (fn [db _]
   (get-in db [::project-dicts :pagination])))

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





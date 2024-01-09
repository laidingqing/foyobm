(ns foyobm.db.basis
  (:require [re-frame.core :as rf]
            [foyobm.db.auth :as auth]))

;; 包含企业及用户方面管理数据

(def initial-company-form
  {:id nil
   :name nil
   :abbr nil
   :address nil
   :telphone nil})

(def initial-state
  {::company {:form initial-company-form}
   ::users {}}
)

;; subs

(rf/reg-sub
 ::current-company
 (fn [db _]
   (get-in db [::company :form])))

;; fx

(rf/reg-event-fx
 ::fetch-current
 (fn [{:keys [db]} [_]]
   (let [token  (get-in db [::auth/auth :account :token])]
     {:fx [[:dispatch [:http {:url "/api/users/current"
                              :method :get
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::fetch-current-success]
                              :on-failure [:http-failure]}]]]})))

(rf/reg-event-fx
 ::fetch-current-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::company :form] (:company data)))}))
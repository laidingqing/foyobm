(ns jidash.db.auth
  (:require [re-frame.core :as rf]
            [jidash.db.ui :as ui]
            [jidash.db.router :as router]))

(def initial-company-form
  {:id nil
   :name nil
   :abbr nil
   :address nil
   :telphone nil})

(def initial-state
  {::auth {}
   ::company {:form initial-company-form}
   ::signin-state :signed-out})

;; subs

(rf/reg-sub
 ::account
 (fn [db]
   (get-in db [::auth :account])))




(rf/reg-sub
 ::signin-state
 (fn [db _]
   (get-in db [::signin-state])))



;; fx


(rf/reg-event-fx
 ::check-identity
 [(rf/inject-cofx :local-storage :account/token)]
 (fn [cofx]
   (let [token (:account/token cofx)]
     {:fx [[:dispatch [:http {:url "/api/users"
                              :method :get
                              :headers {"Authorization" (str "Bearer " token)}
                              :on-success [::auth-success]
                              :on-failure [::logout]}]]]})))

(rf/reg-event-fx
 ::login
 (fn [_ [_ data]]
   {:fx [[:dispatch [::ui/set-dialog :loading]]
         [:dispatch [:http {:url "/api/users/login"
                            :method :post
                            :data data
                            :on-success [::auth-success]
                            :on-failure [:http-failure]}]]]}))


(rf/reg-event-fx
 ::logout
 (fn [{:keys [db]}]
   {:db (assoc-in db [::signin-state] :signed-out)
    :fx [[:set-local-storage [:account/token (:token nil)]]
         [:dispatch [::router/push-state :jidash.render.routes/login]]]}))

(rf/reg-event-fx
 ::auth-success
 (fn [{:keys [db]} [_ data]]
   {:db (-> db
            (assoc-in [::auth :account] data)
            (assoc-in [::signin-state] :signed-in))
    :fx [[:dispatch [::ui/close-dialog]]
         [:set-local-storage [:account/token (:token data)]]
         [:dispatch [::router/push-state :jidash.render.routes/dashboard]]
         [:dispatch [::fetch-current]]]}))

;; subs

(rf/reg-sub
 ::current-company
 (fn [db _]
   (get-in db [::company :form])))

;; fx

(rf/reg-event-fx
 ::fetch-current
 (fn [{:keys [db]} [_ data]]
   (let [token (get-in db [::auth :account :token])] 
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
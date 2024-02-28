(ns jidash.db.core
  (:require [jidash.db.auth :as auth]
            [jidash.db.ui :as ui]
            [jidash.db.router :as router]
            [jidash.db.common :as common]
            [jidash.db.points :as point]
            [re-frame.core :as rf]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]))

(def base-url
  (or js/process.env.BASE_URL "http://localhost:7788"))

(def app-db
  (merge {}
         router/initial-state
         ui/initial-state
         auth/initial-state
         common/initial-state
         point/initial-state))

(rf/reg-event-db
 ::initialize-db
 (fn [] app-db))


(rf/reg-event-fx
 :http
 (fn [_ [_ {:keys [method url data headers on-success on-failure query]}]]
   {:http-xhrio {:method (or method :get)
                 :uri (str base-url url)
                 :params data
                 :url-params query
                 :headers headers
                 :timeout 5000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success on-success
                 :on-failure on-failure}}))



(rf/reg-event-db
 :http-failure
 (fn [db [_ {:keys [response]}]]
   (assoc db ::ui/dialog {:open? true
                          :type :error
                          :message (:message response)})))


(rf/reg-fx
 :set-local-storage
 (fn [[k v]]
   (.setItem (.-localStorage js/window) k v)))

(rf/reg-cofx
 :local-storage
 (fn [cofx k]
   (let [v (.getItem (.-localStorage js/window) k)]
     (assoc cofx k v))))
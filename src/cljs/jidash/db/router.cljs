(ns jidash.db.router
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [reitit.frontend.easy :as rfe]
            [reitit.frontend.controllers :as rfc]))



(def initial-state
  {::router nil})



(rf/reg-fx
 :push-state
 (fn [route]
   (apply rfe/push-state route)))

(rf/reg-event-fx
 ::push-state
 (fn [_ [_ & route]]
   {:fx [[:push-state route]]}))

(rf/reg-event-db
 ::navigated
 (fn [db [_ new-match]]
   (let [old-match (::router db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (assoc db ::router (assoc new-match :controllers controllers)))))

(rf/reg-sub
 ::current-route
 (fn [db] (::router db)))

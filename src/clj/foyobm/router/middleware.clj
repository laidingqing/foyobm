(ns foyobm.router.middleware
  (:require [reitit.ring.coercion :as coercion]
            [ring.middleware.cors :refer [wrap-cors]]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [fitokr.router.exception :as exception]
            [ring.util.request :as request]))


(defmulti authorized? :authorities)
(defmethod authorized? :default [_ _]
  true)

(defmethod authorized? ["ADMIN" "USER"] [request roles]
  ;; (let [user-roles (get-in request [:session :user :roles])]
  ;;   (some #(contains? user-roles %) roles))
  true)

(defn authorize-middleware
  "
   check authorize
  "
  [authorities]
  (fn [handler]
    (fn [request]
      (if (authorized? authorities request)
        (handler request)
        {:status 403
         :body "Access denied"}))))

(def wrap-env
  {:name ::env
   :compile
   (fn [{:keys [env]} _]
     (fn [handler]
       (fn [request]
         (handler (assoc request :env env)))))})

(def global-middleware
  [parameters/parameters-middleware
   muuntaja/format-middleware
   [wrap-cors :access-control-allow-origin [#"http://localhost:3000"]
    :access-control-allow-methods [:get :post :put :delete]]
   muuntaja/format-negotiate-middleware
   muuntaja/format-response-middleware
   muuntaja/format-request-middleware
   exception/exception-middleware
   coercion/coerce-request-middleware
   coercion/coerce-response-middleware
   wrap-env])
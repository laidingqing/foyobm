(ns foyobm.services.router
  (:require [taoensso.timbre :as log]
            [integrant.core :as ig]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [ring.middleware.cors :refer [wrap-cors]]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.coercion.spec :refer [coercion]]
            [foyobm.utils.auth :as auth]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [foyobm.api.core :as api]
            [foyobm.router.middleware :refer [wrap-env]]
            [foyobm.router.exception :as exception]))

(defmethod ig/init-key :reitit/routes
  [_ {:keys [db config]}]
  (let [jwt-secret (:jwt-secret config)]
    (log/info "initializing routes")
    (ring/ring-handler
     (ring/router
      api/routes
      {:data {:env {:db db
                    :jwt-secret (:jwt-secret config)}
              :coercion coercion
              :muuntaja m/instance
              :middleware [parameters/parameters-middleware
                           muuntaja/format-middleware
                           [wrap-cors :access-control-allow-origin [#"http://localhost:3000"]
                            :access-control-allow-methods [:get :post :put :delete]]
                           exception/exception-middleware
                           [wrap-authentication (auth/jwt-backend jwt-secret)]
                           coercion/coerce-response-middleware
                           coercion/coerce-request-middleware
                           wrap-env]}})
     (ring/routes
      (ring/redirect-trailing-slash-handler)))))
(ns jidash.services.router
  (:require [taoensso.timbre :as log]
            [integrant.core :as ig]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [ring.middleware.cors :refer [wrap-cors]]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.coercion.spec :refer [coercion]]
            [jidash.utils.auth :as auth]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [jidash.api.core :as api]
            [jidash.router.middleware :refer [wrap-env]]
            [jidash.router.exception :as exception]
            [jidash.router.frontend :refer [create-frontend-handler]]))

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
                           multipart/multipart-middleware
                           wrap-env]}})
     (ring/routes
      (create-frontend-handler)
      (ring/redirect-trailing-slash-handler)))))
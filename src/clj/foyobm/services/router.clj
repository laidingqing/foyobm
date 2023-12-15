(ns foyobm.services.router
  (:require [taoensso.timbre :as log]
            [integrant.core :as ig]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.coercion.spec :refer [coercion]]
            [foyobm.api.core :as api]
            [foyobm.router.middleware :refer [global-middleware]]
            [foyobm.router.exception :refer [handle-exception]]))

(defmethod ig/init-key :reitit/routes
  [_ {:keys [db config]}]
  (log/info "initializing routes")
  (ring/ring-handler
   (ring/router
    api/routes
    {:data {:env {:db db
                  :jwt-secret (:jwt-secret config)}
            :coercion coercion
            :muuntaja m/instance
            :middleware global-middleware}})
   (ring/routes
    (ring/redirect-trailing-slash-handler)
    (ring/create-default-handler
     {:not-found (handle-exception 404 "Route not found")
      :method-not-allowed (handle-exception 405 "Method not allowed")
      :not-acceptable (handle-exception 406 "Not acceptable")}))))
(ns jidash.services.http
  (:require [taoensso.timbre :as log]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]))

(defmethod ig/init-key :http/server
  [_ {:keys [router config]}]
  (let [port (:http-port config)]
    (log/info "server started on port" port)
    (jetty/run-jetty router {:port port})))

(defmethod ig/halt-key! :http/server
  [_ server]
  (log/info "server stopping")
  (.stop server))
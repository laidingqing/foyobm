(ns jidash.services.db
  (:require [taoensso.timbre :as log]
            [integrant.core :as ig]
            [hikari-cp.core :as hikari]))

(defmethod ig/init-key :postgres/db
  [_ {:keys [config]}]
  (let [{:keys [username password dbname host]} (:jdbc config)
        options {:adapter "postgresql"
                 :username username
                 :password password
                 :database-name dbname
                 :server-name host}]
    (log/info "starting database connection pool starting")
    (log/info options)
    (hikari/make-datasource options)))

(defmethod ig/halt-key! :postgres/db
  [_ ds]
  (log/info "closing database connection pool")
  (hikari/close-datasource ds))
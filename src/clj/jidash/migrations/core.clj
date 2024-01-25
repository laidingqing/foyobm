(ns jidash.migrations.core
  (:require [migratus.core :as migratus]
            [jidash.services.config :refer [read-config]]))

(defn create [config name]
  (migratus/create config name))

(defn migrate [config]
  (migratus/migrate config))

(defn rollback [config]
  (migratus/rollback config))

(defn reset [config]
  (migratus/reset config))

(defn up [config ids]
  (->> ids
       (map #(Long/parseLong %))
       (apply migratus/up config)))

(defn down [config ids]
  (->> ids
       (map #(Long/parseLong %))
       (apply migratus/down config)))


(defn get-store-config-from-jdbc [jdbc]
  (let [{:keys [host dbname username password]} jdbc]
    {:store :database
     :migration-dir "migrations"
     :init-in-transaction? false
     :db {:dbtype "postgresql"
          :host host
          :dbname dbname
          :user username
          :password password}}))


(defn -main
  "run a migrate command with migratus.
   example: clj -M:migrate create create-user"
  [& args]
  (let [command (first args)
        config (read-config)
        conf (get-store-config-from-jdbc (:jdbc (:system/config config)))
        app-confg (:applications-config (:system/config config))]
    ;; (println "luanch migration...")
    ;; (println conf)
    (condp = command
      "create" (create conf (second args))
      "migrate" (migrate conf)
      "rollback" (rollback conf)
      "reset" (reset conf)
      "up" (up conf (rest args))
      "down" (down conf (rest args)))))



(comment
  (def cfg (get-store-config-from-jdbc (:jdbc (:system/config (read-config)))))
  (println (:db cfg))
  (create cfg "create-user"))
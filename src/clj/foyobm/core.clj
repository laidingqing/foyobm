(ns foyobm.core
  (:require [integrant.core :as ig]
            [foyobm.services.config :as config])
  (:gen-class))

(defn -main []
  (let [config (config/read-config :prod)]
    (ig/init config)))
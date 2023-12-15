(ns foyobm.core
  (:require [integrant.core :as ig]
            [foyobm.services.config :as config]))

(defn -main []
  (let [config (config/read-config :prod)]
    (ig/init config)))
(ns foyobm.core
  (:require [integrant.core :as ig]
            [fitokr.services.config :as config]))

(defn -main []
  (let [config (config/read-config :prod)]
    (ig/init config)))
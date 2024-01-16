(ns jidash.core
  (:require [integrant.core :as ig]
            [jidash.services.config :as config])
  (:gen-class))

(defn -main []
  (let [config (config/read-config :prod)]
    (ig/init config)))
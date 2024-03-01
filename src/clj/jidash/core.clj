(ns jidash.core
  (:require [integrant.core :as ig]
            [jidash.services.config :as config]
            [taoensso.timbre :as log])
  (:gen-class))


(def profile (System/getProperty "jidash.run.mode"))

(defn -main []
  (let [config (config/read-config)]
    (log/info "run.mod=" profile)
    (ig/init config)))
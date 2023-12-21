(ns user
  (:require [integrant.repl :as ig-repl]
            [foyobm.services.config :as c]))


(ig-repl/set-prep! (fn []
                     ((c/read-config))))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)







(comment
  (let [db (:db (:config/settings integrant.repl.state/system))]))
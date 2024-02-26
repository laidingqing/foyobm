(ns jidash.render.utils.conversion
  (:require [clojure.string :as s]
            ["dayjs" :as dayjs]))


(defn remove-nils [m]
  (into {} (remove (comp s/blank? second) m)))


(defn dayjs->date-string [d]
  (when d
    (.format (dayjs d) "YYYY-MM-DD HH:MM")))
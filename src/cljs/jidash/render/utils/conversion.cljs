(ns jidash.render.utils.conversion
  (:require [clojure.string :as s]))


(defn remove-nils [m]
  (into {} (remove (comp s/blank? second) m)))
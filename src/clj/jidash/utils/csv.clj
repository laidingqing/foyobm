(ns jidash.utils.csv
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))



(defn read-csv-file [file]
  (let [{:keys [tempfile]} file]
    (with-open [reader (io/reader tempfile)]
      (doall
       (rest
        (csv/read-csv reader))))))

(defn csv-to-map [csv-data]
  (map (fn [[name point title]] {:name name :point (Integer/parseInt point) :title title}) csv-data))
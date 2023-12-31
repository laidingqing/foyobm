(ns foyobm.render.utils.time
  (:require [clojure.string :as str]
            ["moment" :as moment])
  (:import [goog.date Date]))

(defn goog-date->calendar-date [date]
  {:day   (.getDate date)  ;; Between 1-31
   :month (.getMonth date) ;; Between 0-11
   :year  (.getYear date)})

(defn js-date->calendar-date [date]
  {:day   (.getDate date)  ;; Between 1-31
   :month (.getMonth date) ;; Between 0-11
   :year  (.getFullYear date)})

(defn calendar-date->moment-date [{:keys [year month day]}]
  (moment. (js/Date. year month day)))

(defn- format-two-digits [n]
  (if (> n 9)
    (str n)
    (str "0" n)))

(defn calendar-date->string-parts [{:keys [day month year]}]
  [(str year) (format-two-digits (inc month)) (format-two-digits day)])

(defn calendar-date->string [date]
  (str/join "-" (calendar-date->string-parts date)))

(defn string->calendar-date [s]
  (goog-date->calendar-date (Date/fromIsoString s)))

(defn now []
  (js/Date.))

(defn current-calendar-date []
  (js-date->calendar-date (now)))

(defn modify-calendar-date [{:keys [day month year]} n]
  (js-date->calendar-date
   (js/Date. (js/Date.UTC year month (+ day n)))))
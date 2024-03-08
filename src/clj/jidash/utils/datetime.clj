(ns jidash.utils.datetime
  (:require [clj-time.core :as time]
            [clj-time.format :as f]
            [clojure.string :as str]))

(defn extract-date-from-string [s]
  (let [re #"\[\[(.*?)\]\]"
        matches (re-seq re s)
        date-str (first (map second matches))
        now (time/now)]
    (try
      (let [formatter (f/formatter "yyyy/MM/dd")
            date (f/parse formatter date-str)]
        {:date date
         :year (time/year date)
         :month (time/month date)
         :title (str/replace s re "")})
      (catch Exception _
        {:date now
         :year (time/year now)
         :month (time/month now)
         :title (str/replace s re "")}))))


(comment
  (def input "推进创建职业化研发团队 ")
  (def result (extract-date-from-string input))
  (def nv (merge {:title "推进创建职业化研发团队 "} result))
  (println result))
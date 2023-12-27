(ns foyobm.utils.query
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [honey.sql :as sql]
            [taoensso.timbre :as log]))

(defn db-query! [db query]
  (log/info (sql/format query))
  (jdbc/execute! db
                 (sql/format query)
                 {:return-keys true
                  :builder-fn rs/as-unqualified-maps
                  :debug true}))

(defn db-query-one! [db query]
  (log/info (sql/format query))
  (jdbc/execute-one! db
                     (sql/format query)
                     {:return-keys true
                      :builder-fn rs/as-unqualified-maps}))

(defn- make-query
  [f db sql-map]
  (f db
     (sql/format sql-map)
     {:return-keys true
      :builder-fn rs/as-unqualified-maps}))

(defn query!
  [& args]
  (apply make-query jdbc/execute! args))

(defn query-one!
  [& args]
  (apply make-query jdbc/execute-one! args))
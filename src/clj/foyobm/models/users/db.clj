(ns foyobm.models.users.db
  (:require [foyobm.utils.query :as q]
            [integrant.core :as ig]
            [buddy.hashers :refer [encrypt]]))

(defn get-all [db]
  (q/db-query! db {:select [:*]
                   :from [:users]}))


(defn create-user [db data]
  (let [password-hash (encrypt (:password data))
        user (assoc data :password password-hash)]
    
    (q/db-query-one! db {:insert-into :users
                         :values [user]})))


(comment

  (require '[foyobm.services.config :refer [read-config]]
           '[foyobm.services.db]
           '[integrant.repl :as ig-repl :refer [halt]])

  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))

  (def user1 {:password "a123456" :user_name "AsOne" :email "37505218@qq.com" :status 0})
  (println user1)
  (create-user db user1)

  (get-all db)

  (halt)
  )
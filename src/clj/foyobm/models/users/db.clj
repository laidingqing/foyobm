(ns foyobm.models.users.db
  (:require [foyobm.utils.query :as q]
            [integrant.core :as ig]
            [buddy.hashers :refer [encrypt]]
            [taoensso.timbre :as log]))

(defn get-all [db]
  (q/db-query! db {:select [:*]
                   :from [:users]}))


(defn create-user [db data]
  (let [password-hash (encrypt (:password data))
        user (assoc data :password password-hash)]
    
    (q/db-query-one! db {:insert-into :users
                         :values [user]})))

(defn find-user-by-email [db email]
  (log/info "email" email)
  (q/db-query-one! db {:select [:id :email :password :status :fail_count]
                       :from [:users]
                       :where [:= :email email]}))

(defn find-user-by-id [db id]
  ;; (log/info "find-user-by-id:" id)
  (let [user (q/db-query-one! db {:select [:*]
                                  :from [:users]
                                  :where [:= :id id]})]
    (dissoc user :password)))

(comment

  (require '[foyobm.services.config :refer [read-config]]
           '[foyobm.services.db]
           '[integrant.repl :as ig-repl :refer [halt]])

  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))

  (def user1 {:password "a123456" :user_name "AsOne" :email "37505218@qq.com" :status 0})
  (def user2 {:password "a123456" :user_name "laidingqing" :email "24099553@qq.com" :status 0})
  (println user1)
  (create-user db user1)
  (create-user db user2)

  (get-all db)
  (find-user-by-email db "37505218@qq.com")
  (find-user-by-id db 1)
  (halt)
  )
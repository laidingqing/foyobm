(ns foyobm.models.company.db
  (:require [foyobm.utils.query :as q]))



;; companies
(defn get-company-list "query all company list by me, it's test."
  [db]
  (q/db-query! db {:select [:*]
                   :from [:companies]}))

(defn create-company "create company info"
  [db company]
  (q/db-query-one! db {:insert-into :companies
                       :values [company]}))


;; dept_members

(defn create-dept-member 
  [db member]
  (q/db-query-one! db {:insert-into :dept_members
                       :values [member]}))


(defn get-dept-members [db company_id user_id]
  (q/db-query! db {:select [:*]
                   :from [:dept_members]
                   :where [:and [:= :company_id company_id] [:= :user_id user_id]]}))

;; departments

(defn get-departments [db company_id]
  (q/db-query! db {:select [:*]
                   :from [:departments]
                   :where [:= :company_id company_id]}))

(defn create-department "create department info"
  [db department]
  (q/db-query-one! db {:insert-into :departments
                       :values [department]}))


(comment

  (require '[foyobm.services.config :refer [read-config]]
           '[foyobm.services.db]
           '[integrant.core :as ig])

  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))

  (def company1 {:name "福建采控网络科技有限公司" :abbr "采控网络" :create_by 1})
  (create-company db company1)
  (get-company-list db)

  (def dept1 {:company_id 1 :name "研发中心" :position 0 :parent 0 :code "0" :manage_id 1})
  (create-department db dept1)
  (get-departments db 1)

  (def dept_m1 {:company_id 1 :user_id 1 :dept_id 1})
  (create-dept-member db dept_m1)
  (get-dept-members db 1 1)
  ()
  )
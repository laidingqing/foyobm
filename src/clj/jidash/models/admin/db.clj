(ns jidash.models.admin.db
  (:require [jidash.utils.query :as q]
            [taoensso.timbre :as log]))



;; companies
(defn get-company-list "query all company list by me, it's test."
  [db]
  (q/db-query! db {:select [:*]
                   :from [:companies]}))

(defn create-company "create company info"
  [db company]
  (q/db-query-one! db {:insert-into :companies
                       :values [company]}))

(defn find-company-by-name 
  [db name]
  (q/db-query-one! db {:select [:id :name :abbr]
                       :from [:companies]
                       :where [:= :name name]}))


;; company_admins

(defn create-company-admin
  [db admins]
  (q/db-query-one! db {:insert-into :company_admins
                       :values [admins]}))


(defn get-admin-users-by-company
  [db company-id]
  (q/db-query! db {:select [:user_id]
                   :from [:company_admins]
                   :where [:= :company_id company-id]}))

(defn get-company-by-admin-user
  [db user-id]
  (q/db-query-one! db {:select [:company_id]
                       :from [:company_admins]
                       :where [:= :user_id user-id]}))

;; dept_members

(defn create-dept-member 
  [db member]
  (q/db-query-one! db {:insert-into :dept_members
                       :values [member]}))


(defn get-dept-members [db company_id user_id]
  (q/db-query! db {:select [:*]
                   :from [:dept_members]
                   :where [:and [:= :company_id company_id] [:= :user_id user_id]]}))

(defn get-members-by-company-and-dept
  [db company_id dept_id]
  (q/db-query! db {:select [:*]
                   :from [:dept_members]
                   :where [:and [:= :dept_id dept_id] [:= :company_id company_id]]}))
;; departments

(defn get-department-hierarchy 
  "query department and children list."
  [db parent-id]
  (let [root-departments (q/db-query! db {:select [:*]
                                          :from [:departments]
                                          :where [:= :parent parent-id]})
        child-departments (for [dept root-departments] (assoc dept :children (get-department-hierarchy db (:id dept))))]
    child-departments))

(defn get-departments [db company_id]
  (q/db-query! db {:select [:*]
                   :from [:departments]
                   :where [:= :company_id company_id]}))

(defn find-dept-by-parent [db company_id parent]
  (q/db-query-one! db {:select [:*]
                   :from [:departments]
                   :where [:and [:= :company_id company_id] [:= :id parent]]}))


(defn find-dept-and-parent [db company_id parent]
  (q/db-query-one! db {:select [:*]
                       :from [:departments]
                       :where [:and [:= :company_id company_id] [:= :parent parent]]}))

(defn count-dept-by-parent [db parent]
  (q/db-query-one! db {:select [[[:raw "count(*)"] :count]]
                       :from :departments
                       :where [:= :parent parent]}))

(defn create-department "create department info"
  [db department]
  (q/db-query-one! db {:insert-into :departments
                       :values [department]}))


(defn- build-where [{:keys [position company_id]}]
  (let [position-clause [:= :position position]
        company-clause [:= :company_id company_id]]
    (when position
      (cond-> [:and]
        company_id (conj company-clause)
        position (conj position-clause))))
  )

(defn find-dept-list-by-company
  [db query-params]
  (let [query {:select [:id :name :position :parent :code :manage_id :company_id]
               :from [:departments]}
        where-clause (build-where query-params)]
    (q/db-query! db (cond-> query
                      where-clause (assoc :where where-clause)))))


(defn find-company-by-user
  [db user-id]
  (q/db-query-one! db {:select [:c.*]
                       :from [[:dept_members :d]]
                       :where [:= :user_id user-id]
                       :join [[:companies :c] [:= :c.id :d.company_id]]}))


(defn find-member-by-company
  [db company-id]
  (log/info "c-id" company-id)
  (q/db-query! db {:select [:d.* :c.user_name :c.email :c.status]
                       :from [[:dept_members :d]]
                       :where [:= :company_id company-id]
                       :join [[:users :c] [:= :c.id :d.user_id]]}))

(comment

  (require '[jidash.services.config :refer [read-config]]
           '[jidash.services.db]
           '[integrant.core :as ig])

  (def db (:postgres/db (ig/init (dissoc (read-config) :reitit/routes :http/server))))

  (def company1 {:name "福建采控网络科技有限公司" :abbr "采控网络" :create_by 1})
  (create-company db company1)
  (get-company-list db)

  (def dept1 {:company_id 1 :name "研发中心" :position 0 :parent 0 :code "0" :manage_id 1})
  (def dept2 {:company_id 1 :name "市场部" :position 0 :parent 0 :code "0" :manage_id 1})
  (create-department db dept1)
  (create-department db dept2)
  (get-departments db 1)

  (def dept_m1 {:company_id 1 :user_id 1 :dept_id 1})
  (def dept_m2 {:company_id 1 :user_id 2 :dept_id 2})
  (create-dept-member db dept_m1)
  (create-dept-member db dept_m2)

  (get-dept-members db 1 1)
  (get-members-by-company-and-dept db 1 2)
  (find-company-by-name db "婵科技股份有限公司")
  (find-dept-list-by-company db {:parent 0 :company_id 1})


  (def data {:name "dd" :parent 3 :company_id 4})
  (def parent (find-dept-by-parent db 11 0))
  (def count (count-dept-by-parent db (:id parent)))
  (find-dept-by-parent db 11 0)
  (assoc data :company_id 4 :code (str (:position parent) "." (inc (:count  count))) :position (inc (:position parent)))

  (get-department-hierarchy db 0)


  (get-admin-users-by-company db 11)
  (get-company-by-admin-user db 1)
  (find-member-by-company db 11)
  (create-company-admin db {:company_id 11 :user_id 1})
  ()
  )
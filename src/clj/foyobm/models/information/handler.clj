(ns foyobm.models.information.handler
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [foyobm.models.spec :as spec]
            [foyobm.models.information.db :as company.db]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]))

(def default-dept {:position 0 :parent 0 :code "0"})

(defn handle-create-company 
  "创建企业信息"
  [{:keys [env parameters user-id]}]
  (let [{:keys [db]} env
        data (:body parameters)
        valid? (s/valid? ::spec/create-company data)]
    (log/info "create-company " data)
    
    (if valid?
      (let [company (company.db/find-company-by-name db (:name data))
            db-company (assoc data :create_by user-id)]
        (if company
          (rr/response {:error "company-existed."})
          (jdbc/with-transaction [tx db]
            (let [company-id (:id db-company)
                  company-name (:name db-company)
                  db-company (company.db/create-company tx db-company)
                  db-dept (assoc default-dept :company_id company-id :name company-name :manage_id user-id)
                  _ (company.db/create-company-admin tx {:company_id company-id :user_id user-id})
                  res (company.db/create-department tx db-dept)]
              (if res
                (rr/response {:id (:id db-company)})
                (rr/response {:error "insert-company error"}))))))
      (rr/response {:error "create-company form error"}))))


(defn handle-query-departments
  [{:keys [env parameters]}]
  (log/info parameters)
  (let [{:keys [db]} env
        id (get-in parameters [:path :id])
        depts (company.db/find-dept-list-by-company db {:company_id id :parent 0})]
    (if depts
      (rr/response depts)
      (rr/response {:error "query departments error."}))))


(defn handle-create-departments
  "create department info with manager."
  [{:keys [env parameters user-id]}]
  (let [{:keys [db]} env
        data (:body parameters)
        id (get-in parameters [:path :id])
        parent (company.db/find-dept-by-parent db (:company_id data) (:parent data))
        dept (assoc data :company_id id :code "0.1" :position 1)
        res (company.db/create-department db dept)]
    (if res
      (rr/response res)
      (rr/response {:error "create-department error."}))))
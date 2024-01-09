(ns foyobm.models.basis.handler
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [foyobm.models.spec :as spec]
            [foyobm.models.basis.db :as company.db]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]))

(def default-dept {:position 0 :parent 0})

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
            (let [db-company (company.db/create-company tx db-company)
                  company-id (:id db-company)
                  company-name (:name db-company)
                  db-dept (assoc default-dept :company_id company-id :name company-name :manage_id user-id :code (str (:id db-company)))
                  _ (company.db/create-company-admin tx {:company_id company-id :user_id user-id})
                  dept (company.db/create-department tx db-dept)
                  dept_member {:company_id company-id :user_id user-id :dept_id (:id dept)}
                  res (company.db/create-dept-member tx dept_member )]
              (if res
                (rr/response {:id (:id db-company)})
                (rr/response {:error "insert-company error"}))))))
      (rr/response {:error "create-company-form error"}))))


;; {:company_id 1 :user_id 1 :dept_id 1}

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
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)
        ;; id (get-in parameters [:path :id])
        id (:company_id data)
        parent (company.db/find-dept-by-parent db (:company_id data) (:parent data))
        dept_count (company.db/count-dept-by-parent db (:id parent))
        dept (assoc data :company_id id :code (str (:code parent) "." (inc (:count dept_count))) :position (inc (:position parent)))
        res (company.db/create-department db dept)]
    (if res
      (rr/response res)
      (rr/response {:error "create-department error."}))))
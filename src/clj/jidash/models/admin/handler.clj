(ns jidash.models.admin.handler
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [jidash.models.spec :as spec]
            [jidash.models.admin.db :as company.db]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]
            [jidash.models.users.db :as user.db]))

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
                  res (company.db/create-dept-member tx dept_member)]
              (if res
                (rr/response {:id (:id db-company)})
                (rr/response {:error "insert-company error"}))))))
      (rr/response {:error "create-company-form error"}))))


;; {:company_id 1 :user_id 1 :dept_id 1}
;; 仅查询当前企业的一级子项，不含顶级项
(defn handle-query-departments
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        id (get-in parameters [:path :id])

        depts (company.db/find-dept-list-by-company db {:company_id id :position 1})]
    (if depts
      (rr/response depts)
      (rr/response {:error "query departments error."}))))


(defn handle-query-department-members
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        c_id (get-in parameters [:path :id])
        d_id (get-in parameters [:path :did]) 
        query-params (merge (get-in parameters [:query]) {:c_id c_id :d_id d_id})
        members (company.db/get-members-by-company-and-dept db query-params)]
    (if members
      (rr/response members)
      (rr/response {:error "query members error."}))))



(defn handle-create-departments
  "create department info with manager."
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)
        ;; id (get-in parameters [:path :id])
        id (:company_id data)
        parent (company.db/find-dept-and-parent db (:company_id data) (:parent data))
        dept_count (company.db/count-dept-by-parent db (:id parent))
        dept (assoc data :company_id id :code (str (:code parent) "." (inc (:count dept_count))) :position (inc (:position parent)))
        res (company.db/create-department db dept)]
    (if res
      (rr/response res)
      (rr/response {:error "create-department error."}))))


(defn- append-admin-user-id
  [user admins]
  (if (some #(= (:user_id user) (:user_id %)) admins)
    (assoc user :admin true)
    (assoc user :admin false)))


;; 1. 创建用户
;; 
(defn handle-create-member-with-company
  [{:keys [env parameters user-id]}]
  (let [{:keys [db]} env
        data (:body parameters)
        valid? (s/valid? ::spec/create-member data)
        input_company_id (-> data :company_id)
        email (-> data :email)
        password (-> data :password)
        admin? (-> data :admin)
        user-name (-> data :user_name)]
    (if valid?
      (let [company-admin (company.db/get-company-by-admin-user db user-id)
            company-id (-> company-admin :company_id)
            department (company.db/find-dept-and-parent db company-id 0)] 
        (if (not= input_company_id company-id)
          (rr/response {:error "not-access error."})
          (jdbc/with-transaction [tx db]
            (let [db-user (user.db/create-user tx {:email email :password password :user_name user-name})
                  _ (when admin?
                             (company.db/create-company-admin tx {:company_id company-id :user_id user-id}))
                  db-member (company.db/create-dept-member db {:company_id company-id :dept_id (:id department) :user_id (:id db-user)})]
              (if db-member
                (rr/response db-member)
                (rr/response {:error "db error."}))))))
      (rr/response {:error "create-member error."}))))


(defn handle-list-members-with-company
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        query-params (get-in parameters [:query])
        company-id (get-in parameters [:path :id])
        members (company.db/find-member-by-company db company-id query-params)
        admins (company.db/get-admin-users-by-company db company-id)
        members (map #(append-admin-user-id % admins) members)]
    (if members
      (rr/response members)
      (rr/response {:error "list-members error."}))))


(defn handle-list-users-with-company
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        query-params (get-in parameters [:query])
        company-id (get-in parameters [:path :id])
        query-params (merge query-params {:limit 999 :offset 0})
        members (company.db/find-member-by-company db company-id query-params)]
    (if members
      (rr/response members)
      (rr/response {:error "list-members error."}))))
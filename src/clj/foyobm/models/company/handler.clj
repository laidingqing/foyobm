(ns foyobm.models.company.handler
  (:require [clojure.spec.alpha :as s]
            [foyobm.models.spec :as spec]
            [foyobm.models.company.db :as company.db]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]))


(defn handle-create-company [{:keys [env parameters identity]}]
  (let [{:keys [db]} env
        {:keys [id]} identity
        data (:body parameters)]
    (log/info "create-company " data)
    (if (s/valid? ::spec/create-company data)
      (let [company (company.db/find-company-by-name db (:name data) )
            db-company (assoc data :create_by id)
            res (when-not company (company.db/create-company db db-company))]
        (if company
          (rr/response {:error "company is existed."})
          (rr/response res)))
      (rr/response {:error "create-company form error"}))))


(defn handle-query-departments
  [{:keys [env parameters user-id]}]
  (log/info parameters)
  (let [{:keys [db]} env
        id (get-in parameters [:path :id])
        depts (company.db/find-dept-list-by-company db id {})]
    (if depts
      (rr/response depts)
      (rr/response {:error "query departments error."}))))


(defn handle-create-departments [{:keys [env parameters user-id]}]
  (rr/response ""))
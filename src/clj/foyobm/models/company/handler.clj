(ns foyobm.models.company.handler
  (:require [clojure.spec.alpha :as s]
            [foyobm.models.spec :as spec]
            [foyobm.models.company.db :as company.db]
            [ring.util.response :as rr]))


(defn handle-create [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)]
    (if (s/valid? ::spec/create-company data)
      (let [company (company.db/find-company-by-name db name)
            res (when-not company (company.db/create-company db data))]
        (if company
          (rr/response {:error "company is existed."})
          (rr/response res)))
      (rr/response {:error "User login error"}))))
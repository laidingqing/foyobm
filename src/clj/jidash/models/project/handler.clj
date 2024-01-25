(ns jidash.models.project.handler
  (:require [clojure.spec.alpha :as s]
            [jidash.models.project.db :as project.db]
            [jidash.models.spec :as spec]
            [ring.util.response :as rr]))

(defn handle-list-projects
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        query-params (get-in parameters [:query])
        projects (project.db/get-all-projects db query-params)]
    (if projects
      (rr/response projects)
      (rr/response {:error "project-error"}))))


(defn handle-list-applications
  [{:keys [env _]}]
  (let [{:keys [_ applications-config]} env]
    (rr/response applications-config)))


(defn handle-list-project-dicts
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        query-params (get-in parameters [:query])
        dicts (project.db/get-all-project-dcits db query-params)]
    (rr/response dicts)))

(defn handle-create-project-dicts
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)
        res (project.db/create-project-dict db data)]
    (rr/response res)))

(defn handle-create-or-update-project
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)
        name (-> data :name)
        company-id (-> data :company_id)
        existed? (project.db/find-project-by-name-and-company db name company-id)]
    (if (s/valid? ::spec/create-project data)
      (let [res (if existed?
                  (project.db/update-project db data (:id existed?))
                  (project.db/create-project db data))]
        (rr/response {:id (:id res)})
        )
      (rr/response {:error "project-create error."}))))
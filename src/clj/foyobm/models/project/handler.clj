(ns foyobm.models.project.handler
  (:require [clojure.spec.alpha :as s]
            [foyobm.models.project.db :as project.db]
            [foyobm.models.spec :as spec]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]))


(defn- project-valid? [config name]
  
  )


(defn handle-list-projects 
  [{:keys [env _]}]
  (let [{:keys [db]} env
        projects (project.db/get-all-projects db)]
    (if projects
      (rr/response projects)
      (rr/response {:error "project-error"}))
    )
  )

(defn handle-list-applications
  [{:keys [env _]}]
  (let [{:keys [_ applications-config]} env]
    (rr/response applications-config)))

(defn handle-create-project
  [{:keys [env parameters]}]
  (let [{:keys [db project-config]} env
        data (:body parameters)]
    (if (s/valid? ::spec/create-project data)
      (let [project (project-valid? project-config (:name data))
            res (when project (project.db/create-project db data))]
        (if res
          (rr/response {:id (:id res)})
          (rr/response {:error "project-error"})))
      (rr/response {:error "project-create error."}))))
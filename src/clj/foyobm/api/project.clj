(ns foyobm.api.project
  (:require [foyobm.models.project.handler :refer [handle-list-projects 
                                                   handle-list-applications
                                                   handle-create-or-update-project]]
            [foyobm.router.middleware :refer [wrap-authorization]]
            [foyobm.models.spec :as spec]))


(def project-route
  ["/projects"
   ["" {:get {:summary "find my project list with company"
              :middleware [wrap-authorization]
              :parameters {:query ::spec/query-projects}
              :handler handle-list-projects}
        :post {:summary "add project to company."
               :middleware [wrap-authorization]
               :parameters {:body ::spec/create-project}
               :handler handle-create-or-update-project}}] 
   ["/applications" {:get {:summary "查询可添加的积分项目，源于配置"
                           :handler handle-list-applications}}]])
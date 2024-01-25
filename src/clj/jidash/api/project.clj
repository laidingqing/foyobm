(ns jidash.api.project
  (:require [jidash.models.project.handler :refer [handle-list-projects 
                                                   handle-list-applications
                                                   handle-create-or-update-project
                                                   handle-list-project-dicts
                                                   handle-create-project-dicts]]
            [jidash.router.middleware :refer [wrap-authorization]]
            [jidash.models.spec :as spec]))


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
                           :handler handle-list-applications}}]
   ["/dicts" {:get {:summary "查询所有项目定制项"
                    :parameters {:query ::spec/query-dicts}
                    :handler handle-list-project-dicts}
              :post {:summary "创建项目定制项"
                     :handler handle-create-project-dicts}}]])
(ns foyobm.api.project
  (:require [foyobm.models.project.handler :refer [handle-list-projects handle-list-applications]]
            [foyobm.router.middleware :refer [wrap-authorization]]))



(def project-route
  ["/projects"
   ["" {:get {:summary "find my project list with company"
              :middleware [wrap-authorization]
              :handler handle-list-projects}}] 
   ["/applications" {:get {:summary "查询可添加的积分项目，源于配置"
                           :handler handle-list-applications}}]])
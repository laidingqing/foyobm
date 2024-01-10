(ns foyobm.api.basis
  (:require [foyobm.models.basis.handler :refer [handle-create-company 
                                                 handle-query-departments 
                                                 handle-create-departments
                                                 handle-list-members-with-company]]
            [foyobm.router.middleware :refer [wrap-authorization]]
            [foyobm.models.spec :as spec]))



(def basis-route
  ["/basis"
   ["/companies"
    ["" {:post {:summary "create a company info"
                :middleware [wrap-authorization]
                :parameters {:body ::spec/create-company}
                :handler handle-create-company}}]
    ["/:id"
     ["/members" {:get {:summary "query members by company"
                        :middleware [wrap-authorization]
                        :parameters {:path {:id int?}}
                        :handler handle-list-members-with-company}}]
     ["/departments"
      ["" {:post {:summary "create a department info in company"
                  :middleware [wrap-authorization]
                  :parameters {:path {:id int?} :body ::spec/create-department}
                  :handler handle-create-departments}
           :get {:summary "query department list with company"
                 :middleware [wrap-authorization]
                 :parameters {:path {:id int?}}
                 :handler handle-query-departments}}]]]]])
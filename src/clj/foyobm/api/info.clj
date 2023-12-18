(ns foyobm.api.info
  (:require [foyobm.models.company.handler :refer [handle-create-company handle-query-departments handle-create-departments]]
            [foyobm.router.middleware :refer [wrap-authorization]]
            [foyobm.models.spec :as spec]))



(def info-route
  ["/info"
   ["/companies"
    ["" {:post {:summary "create a company info"
                :middleware [wrap-authorization]
                :parameters {:body ::spec/create-company}
                :handler handle-create-company}}]
    ["/:id"
     ["/departments"
      ["" {:post {:summary "create a department info in company"
                  :middleware [wrap-authorization]
                  :parameters {:path {:id int?}}
                  :handler handle-create-departments}
           :get {:summary "query department list with company"
                 :middleware [wrap-authorization]
                 :parameters {:path {:id int?}}
                 :handler handle-query-departments}}]]]]])
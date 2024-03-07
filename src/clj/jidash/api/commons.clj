(ns jidash.api.commons
  (:require [jidash.models.admin.handler :refer [handle-create-company 
                                                 handle-query-departments 
                                                 handle-create-departments
                                                 handle-list-members-with-company
                                                 handle-create-member-with-company
                                                 handle-list-users-with-company
                                                 handle-query-department-members]]
            [jidash.router.middleware :refer [wrap-authorization]]
            [jidash.models.spec :as spec]))



(def commons-route
  ["/commons"
   ["/companies"
    ["" {:post {:summary "create a company info"
                :middleware [wrap-authorization]
                :parameters {:body ::spec/create-company}
                :handler handle-create-company}}]
    ["/:id"
     ["/members" {:get {:summary "query members by company"
                        :middleware [wrap-authorization]
                        :parameters {:path {:id int?} :query {:limit int? :offset int?}}
                        :handler handle-list-members-with-company}
                  :post {:summary "create member by company"
                         :middleware [wrap-authorization]
                         :parameters {:path {:id int?}
                                      :body ::spec/create-member}
                         :handler handle-create-member-with-company}}]
     ["/users" {:get {:summary "query members by company"
                      :middleware [wrap-authorization]
                      :parameters {:path {:id int?} :query {:limit int? :offset int?}}
                      :handler handle-list-users-with-company}}]
     ["/departments"
      ["" {:post {:summary "create a department info in company"
                  :middleware [wrap-authorization]
                  :parameters {:path {:id int?} :body ::spec/create-department}
                  :handler handle-create-departments}
           :get {:summary "query department list with company"
                 :middleware [wrap-authorization]
                 :parameters {:path {:id int?}}
                 :handler handle-query-departments}}]
      ["/:did/members" {:get {:summary "query all members with department's id"
                              :middleware [wrap-authorization]
                              :parameters {:path {:id int? :did int?} :query {:limit int? :offset int?}}
                              :handler handle-query-department-members
                              }}]]]]])
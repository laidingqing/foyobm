(ns jidash.api.okr
  (:require [jidash.router.middleware :refer [wrap-authorization]]
            [jidash.models.spec :as spec]
            [jidash.models.okr.handler :refer [create-simply-okr 
                                               handle-query-objectives
                                               handle-get-objective
                                               create-key-results]]))


(def okr-routes
  ["/okrs"
   ["" {:post {:middleware [wrap-authorization]
               :parameters {:body ::spec/create-objective}
               :handler create-simply-okr}
        :get {:middleware [wrap-authorization]
              :parameters {:query {:c_id int? :u_id int? :limit int? :offset int?} }
              :handler handle-query-objectives}}]
   ["/:id" {:get {:middleware [wrap-authorization]
                  :parameters {:path {:id int?}}
                  :handler handle-get-objective}}
    ["/key-results" {:post {:middleware [wrap-authorization]
                            :parameters {:path {:id int?} :body ::spec/create-key-result}
                            :handler create-key-results}}]]])
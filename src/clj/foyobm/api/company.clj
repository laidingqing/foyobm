(ns foyobm.api.company
  (:require [foyobm.models.company.handler :refer [handle-create]]))



(def company-route 
  ["/companies"
   ["" {:post {:summary "create a company info"
               :handler handle-create}}]])
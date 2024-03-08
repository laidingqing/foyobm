(ns jidash.api.okr
  (:require [jidash.router.middleware :refer [wrap-authorization]]
            [jidash.models.spec :as spec]
            [jidash.models.okr.handler :refer [create-simply-okr]]))


(def okr-routes
  ["/okrs"
   ["" {:post {:middleware [wrap-authorization]
              :handler create-simply-okr}}]])
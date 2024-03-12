(ns jidash.api.activity
  (:require [jidash.models.activity.handler :refer [handle-query-activities handle-create-activity handle-batch-create-activity]]
            [jidash.router.middleware :refer [wrap-authorization]]
            [jidash.models.spec :as spec]
            [reitit.ring.middleware.multipart :as multipart]))



(def activity-routes
  ["/activities"
   ["" {:get {:middleware [wrap-authorization]
              :parameters {:query ::spec/query-activities}
              :handler handle-query-activities}
        :post {:middleware [wrap-authorization]
               :parameters {:body ::spec/create-activity}
               :handler handle-create-activity}}]
   ["/upload" {:post {:middleware [wrap-authorization]
               :parameters {:multipart {:file multipart/temp-file-part}}
               :handler handle-batch-create-activity}}]])
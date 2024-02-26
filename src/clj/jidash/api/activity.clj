(ns jidash.api.activity
  (:require [jidash.models.activity.handler :refer [handle-query-activities handle-create-activity]]
            [jidash.router.middleware :refer [wrap-authorization]]
            [jidash.models.spec :as spec]))



(def activity-routes
  ["/activities"
   ["" {:get {:middleware [wrap-authorization]
              :handler handle-query-activities}
        :post {:middleware [wrap-authorization]
               :parameters {:body ::spec/create-activity}
               :handler handle-create-activity}}]])
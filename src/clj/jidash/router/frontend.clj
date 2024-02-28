(ns jidash.router.frontend
  (:require [ring.util.response :as response]))

(defn create-frontend-handler []
  (fn [req]
    (or (response/resource-response (:uri req) {:root "public"})
        (-> (response/resource-response "index.html" {:root "public"})
            (response/content-type "text/html")))))

(ns foyobm.models.webhook.handler
  (:require [ring.util.response :as rr]
            [taoensso.timbre :as log]))


(defn handler-webhook-ingest
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)]
    (log/info "ingest: " data)
    (if true
      (rr/response {:message "Ok"})
      (rr/response {:error "ingest-error"}))))
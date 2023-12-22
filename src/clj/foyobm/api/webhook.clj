(ns foyobm.api.webhook
  (:require [foyobm.models.webhook.handler :refer [handler-webhook-ingest]]
            [foyobm.models.spec :as spec]))

;; 用于获取相关项目的原始数据，如JIRA/Conference

;; 根据: headers "X-BM-Event" 区分项目分类
(def webhook-routes
  ["/webhooks"
   ["/:app" {:post {:handler handler-webhook-ingest
               :parameters {:path {:app string?} :body ::spec/webhook}}}]])
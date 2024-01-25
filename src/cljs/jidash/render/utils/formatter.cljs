(ns jidash.render.utils.formatter 
  (:require [jidash.render.components.antd :as antd]))



(defn user-status-formatter [status]
  (condp = status
        0 (antd/text "正常"))
  )


(defn user-admin-formatter [record]
  (if (:admin record)
    (antd/text "是")
    (antd/text "-")))
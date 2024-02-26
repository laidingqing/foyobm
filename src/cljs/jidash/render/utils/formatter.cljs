(ns jidash.render.utils.formatter 
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.utils.conversion :refer [dayjs->date-string]]))



(defn user-status-formatter [status]
  (condp = status
        0 (antd/text "正常"))
  )


(defn user-admin-formatter [record]
  (if (:admin record)
    (antd/text "是")
    (antd/text "-")))


(defn activity-catalog-formatter [record]
  (condp = (:catalog record)
        "manual" (antd/text "手动变更")))

(defn datetime-formatter [d]
  (dayjs->date-string d))

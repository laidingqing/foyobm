(ns jidash.models.spec
  (:require [clojure.spec.alpha :as s]))


;;common spec
(s/def ::id integer?)
(s/def ::limit (s/and int? #(>= % 0)))
(s/def ::offset (s/and int? #(>= % 0)))
(s/def ::company_id integer?)
(s/def ::c_id string?)
(s/def ::activated boolean?)

;;user spec
(s/def ::email string?)
(s/def ::password string?)

(s/def ::register-user (s/keys :req-un [::email ::password ::user_name]))
(s/def ::login-user (s/keys :req-un [::email ::password]))

;; company sepc

(s/def ::create-company (s/keys :req-un [::name ::abbr]))
(s/def ::create-department (s/keys :req-un [::name ::parent ::company_id]))

;; member spec

(s/def ::create-member (s/keys :req-un [::email ::user_name ::password ::admin ::company_id]))
;; webhook
(s/def ::issue map?)
(s/def ::worklog map?)
(s/def ::webhook (s/keys :req-un [(s/nilable ::issue) (s/nilable ::worklog)]))


;; activity
(s/def ::title string?)
(s/def ::name string?)
(s/def ::catalog string?)
(s/def ::score int?)
(s/def ::data string?)
(s/def ::create-activity (s/keys :req-un [::user_id ::company_id ::score ::title ::catalog]))
(s/def ::create-batch-activity (s/keys :req-un [::catalog ::title ::data])) ;; title为变更标题模版，可加%s data字符格式: 人名 分数
(s/def ::query-activities (s/keys :req-un [(s/nilable ::user_id) (s/nilable ::limit) (s/nilable ::offset)]))


(comment
  (print (s/valid? ::create-company {:name "婵科技股份有限公司" :abbr "婵科技"}))
  )
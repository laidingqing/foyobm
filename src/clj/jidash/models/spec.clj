(ns jidash.models.spec
  (:require [clojure.spec.alpha :as s]))


;;common spec
(s/def ::id integer?)
(s/def ::limit (s/and int? #(>= % 0)))
(s/def ::offset (s/and int? #(>= % 0)))
(s/def ::company_id integer?)
(s/def ::c_id string?)
(s/def ::activated boolean?)
(s/def ::user_id int?)

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

(s/def ::year int?)
(s/def ::month int?)
(s/def ::day int?)

(s/def ::create-activity (s/keys :req-un [::user_id ::company_id ::score ::title ::catalog]))
(s/def ::query-activities (s/keys :req-un [::user_id ::limit  ::offset]))
(s/def ::query-summary (s/keys :req-un [::user_id ::year ::month (s/nilable ::day)]))

;; OKRs
(s/def ::tv double?)
(s/def ::unit string?)
(s/def ::create-objective (s/keys :req-un [::user_id ::company_id ::title]))
(s/def ::create-key-result (s/keys :req-un [::o_id ::user_id ::company_id ::title ::tv ::unit]))



(comment
  (print (s/valid? ::create-company {:name "婵科技股份有限公司" :abbr "婵科技"}))
  )

(ns jidash.models.spec
  (:require [clojure.spec.alpha :as s]))


;;common spec
(s/def ::id integer?)
(s/def ::limit (s/and int? #(>= % 0)))
(s/def ::offset (s/and int? #(>= % 0)))
(s/def ::company_id integer?)
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


;; projects
(s/def ::create-project (s/keys :req-un [::name ::datalog ::company_id ::activated]))
(s/def ::query-projects (s/keys :req-un [::limit ::offset ::company_id]))
(s/def ::query-dicts (s/keys :req-un [(s/nilable ::classv) (s/nilable ::limit) (s/nilable ::offset) ]))

(comment
  (print (s/valid? ::create-company {:name "婵科技股份有限公司" :abbr "婵科技"}))
  )
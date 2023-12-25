(ns foyobm.models.spec
  (:require [clojure.spec.alpha :as s]))


;;common spec
(s/def ::id integer?)


;;user spec
(s/def ::email string?)
(s/def ::password string?)

(s/def ::register-user (s/keys :req-un [::email ::password ::user_name]))
(s/def ::login-user (s/keys :req-un [::email ::password]))

;; company sepc

(s/def ::create-company (s/keys :req-un [::name ::abbr]))
(s/def ::create-department (s/keys :req-un [::name ::parent ::company_id]))


;; webhook
(s/def ::issue map?)
(s/def ::worklog map?)
(s/def ::webhook (s/keys :req-un [(s/nilable ::issue) (s/nilable ::worklog)]))


;; project
(s/def ::create-project (s/keys :req-un [::name ::datalog ::description]))

(comment
  (print (s/valid? ::create-company {:name "婵科技股份有限公司" :abbr "婵科技"}))
  )
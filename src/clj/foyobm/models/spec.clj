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

(s/def ::create-company (s/keys :req-un [::name ::abbr ::user_id]))
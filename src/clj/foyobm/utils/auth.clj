(ns foyobm.utils.auth
  (:require [buddy.auth.backends :as backends]
            [buddy.hashers :refer [check]]
            [buddy.sign.jwt :as jwt]))

(defn jwt-backend
  [secret]
  (backends/jws {:secret secret
                 :token-name "Bearer"}))

(defn user->response
  [user secret]
  (let [sanitized (dissoc user :password)
        token (jwt/sign sanitized secret)]
    (assoc sanitized :token token)))

(defn password-match?
  [user password]
  (when (and user
             (check password (:password user)))
    user))

(comment
  (def a {:id 3 :authority "ADMIN,USER" :email "37505218@qq.com" :password "bcrypt+sha512$41996019ad62f1c09bdd1b88734f42eb$12$ed2b6e4b45cde21340e9f97a48fdcf7f38b8fc6914b2c08d"})
  (vals a)
  (get a :email)
  (:authority a)
  (user->response a "cc")

  (check "a123456" "bcrypt+sha512$5648559ff3882867aa84db7059767614$12$0a6c3cf203cb44c3c5f32999ae233db818723c70c7d0bed8")

  (password-match? a "a123456"))
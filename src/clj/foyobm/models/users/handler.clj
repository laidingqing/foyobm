(ns foyobm.models.users.handler
  (:require [clojure.spec.alpha :as s]
            [foyobm.utils.auth :as auth]
            [foyobm.models.users.db :as user.db]
            [foyobm.models.spec :as spec]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]))

(defn handle-login [{:keys [env parameters]}]
  (let [{:keys [db jwt-secret]} env
        {:keys [email password]} (:body parameters)]
    (if (s/valid? ::spec/login-user {:email email :password password})
      (let [user (user.db/find-user-by-email db email)
            user (auth/password-match? user password)
            res (when user
                  (auth/user->response user jwt-secret))]
        (log/info user)
        (if res
          (rr/response res)
          (rr/response {:error "Invalid credentials"})))
      (rr/response {:error "User login error"}))))
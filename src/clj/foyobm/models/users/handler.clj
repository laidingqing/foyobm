(ns foyobm.models.users.handler
  (:require [clojure.spec.alpha :as s]
            [foyobm.utils.auth :as auth]
            [foyobm.models.users.db :as user.db]
            [foyobm.models.basis.db :as basis.db]
            [foyobm.models.spec :as spec]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]))


(defn get-current-info
  [{:keys [env identity]}]
  (let [{:keys [email]} identity
        {:keys [db]} env
        user (user.db/find-user-by-email db email)
        current-info (when user (-> user
                                    (dissoc :password)
                                    (assoc :company (basis.db/find-company-by-user db (:id user)))))]
    (if current-info
      (rr/response current-info)
      (rr/response {:error "Malformed token"}))))




(defn handle-user-login [{:keys [env parameters]}]
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



(defn handle-create-user
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)]
    (if (s/valid? ::spec/register-user data)
      (let [user (user.db/find-user-by-email db (:email data))
            res (when-not user (user.db/create-user db data))]
        (if user
          (rr/response {:error "user-existed"})
          (rr/response {:id (:id res)})))
      (rr/response {:error "registe-user error."})))
  )
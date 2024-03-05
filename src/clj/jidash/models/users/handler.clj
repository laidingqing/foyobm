(ns jidash.models.users.handler
  (:require [clojure.spec.alpha :as s]
            [jidash.utils.auth :as auth]
            [jidash.models.users.db :as user.db]
            [jidash.models.admin.db :as basis.db]
            [jidash.models.spec :as spec]
            [ring.util.response :as rr]
            [taoensso.timbre :as log]))


(defn get-account
  [{:keys [env identity]}]
  (let [{:keys [email]} identity
        {:keys [db jwt-secret]} env
        user (user.db/find-user-by-email db email)
        user-id (:id user)
        admin (basis.db/get-company-by-admin-user db user-id)
        admin? (when admin
                 (= user-id (:user_id admin)))
        res (when user
              (-> user
                  (merge {:admin admin?})
                  (auth/user->response jwt-secret)))]
    (if res
      (rr/response res)
      (rr/response {:error "Malformed token"}))))


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
            user-id (:id user)
            user (auth/password-match? user password)
            admin (basis.db/get-company-by-admin-user db user-id)
            admin? (when admin
                     (= user-id (:user_id admin)))
            res (when user
                  (-> user
                      (merge {:admin admin?})
                      (auth/user->response jwt-secret))
                  )]
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
      (rr/response {:error "registe-user error."}))))
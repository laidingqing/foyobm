(ns jidash.api.users
  (:require [jidash.models.users.handler :refer [handle-user-login get-current-info handle-create-user get-account]]
            [jidash.router.middleware :refer [wrap-authorization]]
            [jidash.models.spec :as spec]))


(def user-routes
  ["/users"
   ["" {:get {:middleware [wrap-authorization]
              :handler get-account}}]
   ["/current" {:get {:middleware [wrap-authorization]
              :handler get-current-info}}]
   ["/login" {:post {:summary "login with email and password"
                     :handler handle-user-login
                     :parameters {:body ::spec/login-user}}}]
   ["/signup" {:post {:summary "register user"
                      :handler handle-create-user
                      :parameters {:body ::spec/register-user}}}]])
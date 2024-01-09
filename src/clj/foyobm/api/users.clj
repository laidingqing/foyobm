(ns foyobm.api.users
  (:require [foyobm.models.users.handler :refer [handle-user-login get-current-info handle-create-user]]
            [foyobm.router.middleware :refer [wrap-authorization]]
            [foyobm.models.spec :as spec]))


(def user-routes
  ["/users"
   ["/current" {:get {:middleware [wrap-authorization]
              :handler get-current-info}}]
   ["/login" {:post {:summary "login with email and password"
                     :handler handle-user-login
                     :parameters {:body ::spec/login-user}}}]
   ["/signup" {:post {:summary "register user"
                      :handler handle-create-user
                      :parameters {:body ::spec/register-user}}}]])
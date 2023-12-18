(ns foyobm.api.users
  (:require [foyobm.models.users.handler :refer [handle-login check-identity]]
            [foyobm.router.middleware :refer [wrap-authorization]]
            [foyobm.models.spec :as spec]))


(def user-routes
  ["/users"
   ["/me" {:get {:middleware [wrap-authorization]
              :handler check-identity}}]
   ["/login" {:post {:summary "login with email and password"
                     :handler handle-login
                     :parameters {:body ::spec/login-user}}}]])
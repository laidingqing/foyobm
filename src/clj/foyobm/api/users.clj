(ns foyobm.api.users
  (:require [foyobm.models.users.handler :refer [handle-login]]
            [foyobm.models.spec :as spec]))


(def user-routes
  ["/users"
   ["/login" {:post {:summary "login with email and password"
                     :handler handle-login
                     :parameters {:body ::spec/login-user}}}]])
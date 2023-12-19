(ns foyobm.router.middleware
  (:require [buddy.auth :refer [authenticated?]]
            [foyobm.models.users.db :as user.db]
            [foyobm.router.exception :as exception]
            [taoensso.timbre :as log])
  (:import [java.util UUID]))



(def wrap-env
  {:name ::env
   :compile
   (fn [{:keys [env]} _]
     (fn [handler]
       (fn [request]
         (handler (assoc request :env env)))))})

;; 根据请求头中的token获取用户会话信息，handle中可获取user-id值
(def wrap-authorization
  {:name ::authorization
   :wrap
   (fn
     [handler]
     (fn [{:keys [env] :as request}]
       (if (authenticated? request)
         (let [{:keys [db]} env
               user-id (get-in request [:identity :id])
               user (user.db/find-user-by-id db user-id)]
           (if user
             (handler (assoc request
                             :user-id user-id))
             (exception/response 401 "Malformed token" request)))
         (exception/response 401 "Unauthorized" request))))})
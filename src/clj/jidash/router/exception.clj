(ns jidash.router.exception
  (:require [taoensso.timbre :as log]
            [reitit.coercion :as coercion]
            [reitit.ring.middleware.exception :as exception]))

(defn exception-response
  [status message exception request]
  {:status status
   :body (merge {:success false
                 :message message
                 :uri (:uri request)}
                (when exception
                  {:exception (.getClass exception)
                   :data (ex-data exception)}))})


(defn response
  ([status message request]
   (response status message request nil))
  ([status message request exception]
   (log/error exception)
   {:status status
    :body (merge
           {:success false
            :message message
            :uri (:uri request)}
           (when exception
             exception))}))

(defn not-found [request]
  (response 404 "Resource not found" request))



(defn handle-exception [status message]
  (fn
    ([request]
     (exception-response status message nil request))
    ([exception request]
     (exception-response status message exception request))))

(defn handle-coercion-exception [status message]
  (fn [exception request]
    (exception-response status
                        message
                        (coercion/encode-error (ex-data exception))
                        request)))

(def exception-middleware
  (exception/create-exception-middleware
   {::exception/default (handle-exception 500 "Internal server error")
    :muuntaja/decode (handle-exception 400 "Malformed request")
    ::coercion/request-coercion (handle-coercion-exception 400 "Malformed request")
    ::coercion/response-coercion (handle-coercion-exception 500 "Malformed response")
    java.sql.SQLException (handle-exception 500 "Database exception")
    ::exception/wrap (fn [handler e request]
                       (log/error e (:uri request))
                       (handler e request))}))
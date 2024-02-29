(ns jidash.plugins.jira.core
  (:require [jidash.plugins.core :refer [PointSource]]
            [ring.util.response :as rr]
            [clojure.java.io :as io]
            [clj-yaml.core :as yaml]
            [taoensso.timbre :as log]
            [jidash.models.points.db :as point.db]
            [jidash.models.users.db :as user.db]
            [jidash.models.activity.db :as activity.db]
            [jidash.models.admin.db :as admin.db]
            [jidash.models.spec :as spec]
            [next.jdbc :as jdbc]))

(defn get-value [data path]
  (get-in data (map keyword path)))

(defn load-jira-config []
  (into {} (yaml/parse-string (slurp (io/resource "jira.yaml")))))



(defn eval-jira-rule-value [key config data]
  (let [user-name-path (get-in config [:user :name])
        user-email-path (get-in config [:user :email])
        user-name (get-value data user-name-path)
        user-email (get-value data user-email-path)
        title (get-in config [:point_rules (keyword key) :title])
        description (get-in config [:point_rules (keyword key) :description])
        path_keys (get-in config [:point_rules (keyword key) :field_path])
        param-values (map #(get-value data %) path_keys)
        condition (get-in config [:point_rules (keyword key) :condition])
        rule (eval (read-string condition))
        result? (apply rule param-values)]
    (if result?
      (let [issue-key (get-value data [:issue :key])
            point-path (get-in config [:point_rules (keyword key) :points :field_path])
            point-value (get-value data point-path)
            point-values (get-in config [:point_rules (keyword key) :points :point])
            point (get-in point-values [(keyword point-value)])
            title (format title issue-key point-value point)]
        {:issue issue-key :point point :title title :description description :user-name user-name :email user-email})
      nil)))


(defn store-jira-point [db point]
  (log/info point)
  (let [{:keys [user-name title point]} point
        db-user (user.db/find-user-by-name db user-name)]
    (log/info db-user)
    (when db-user
      (jdbc/with-transaction [tx db]
        (log/info "change user's point by jira")
        (let [user_name (:user_name db-user)
              user-id (:id db-user)
              db-company (admin.db/find-company-by-user tx user-id)
              db-user-point (point.db/query-user-point tx user-id)
              _ (activity.db/create-activity tx {:user_id user-id :pre_score (:points db-user-point) :score point :title title :catalog "jira" :name user_name})

              last-point (+ (:points db-user-point) point)
              db-point (point.db/update-user-point tx user-id {:user_id user-id :company_id (:id db-company) :points last-point})]
          (if db-point
            db-point
            nil)))))
  )

(defn jira-handler
  [{:keys [env parameters]}]
  (let [{:keys [db]} env
        data (:body parameters)
        config (load-jira-config)
        values (map #(eval-jira-rule-value % config data) (keys (get-in config [:point_rules])))
        values (filter #(not (nil? %)) values)
        r (map #(store-jira-point db %) values)]
    (log/info "jira:" data)
    (dorun r)
    (rr/response values)))


;; Jira
;; 实现JIRA的webhook接收路由及积分逻辑
(defrecord JiraPointSource []
  PointSource
  (load-source [this])
  (handler [this]
    [["/jira" {:post {:parameters {:body ::spec/webhook}
                      :handler jira-handler}}]]))










(comment
  (def c (load-jira-config))
  (println (get-in c [:point_rules]))
  (def data {:issue {:fields {:status {:name "完成"}
                              :issuetype {:name "故障"}
                              :customfield_10300 {:value "一般错误"}}}})

  (map (fn [v] (println v)) (keys (get-in c [:point_rules])))


  (def finished_path (get-in c [:point_rules :bug_appeared :field_path]))
  (def bug_level_path (get-in c [:point_rules :bug_appeared :points :field_path]))
  (println bug_level_path)

  (def bug_level (get-value data bug_level_path))
  (println bug_level)

  (def bug_level_values (get-in c [:point_rules :bug_appeared :points :point]))
  (println bug_level_values)

  (println (get-in bug_level_values [(keyword bug_level)])) ;; -3


  (def condition (get-in c [:point_rules :bug_appeared :condition]))
  (println (count finished_path))
  (println (nth finished_path 0))


  (println (get-value data (nth finished_path 0))) ;;

  (def rule (eval (read-string condition)))

  (def p ["故障" "完成"])
  (apply rule p)


  (keys (get-in c [:point_rules]))

  (map (fn [k]
         (let [path_keys (get-in c [:point_rules (keyword k) :field_path])
               values (map (fn [k] (get-value data k)) path_keys)
               condition (get-in c [:point_rules (keyword k) :condition])
               rule (eval (read-string condition))
               result? (apply rule values)]
           
           (println result?)
           
           ))
       (keys (get-in c [:point_rules])))

  )
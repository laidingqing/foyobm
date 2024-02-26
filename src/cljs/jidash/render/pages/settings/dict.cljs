(ns jidash.render.pages.settings.dict
  (:require [re-frame.core :as rf]
            [jidash.db.settings :as settings] 
            [jidash.render.components.antd :as antd]
            [jidash.render.utils.antd :refer [row-key]]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]))

(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/alert {:type "info" :message "设置项目积分项"})])

(defn- dict-table-header []
  (antd/space {:style {:margin-bottom "16px"}}
              (antd/button {:type "primary" :style {:marginBottom "20px"} :onClick #(rf/dispatch [::settings/new-project-dict-form])} "创建规则项")))

(def columns [{:title "编号" :key "id" :dataIndex "id"}
              {:title "项目" :key "description" :dataIndex "description"}
              {:title "编码" :key "code" :dataIndex "code"}
              {:title "名称" :key "title" :dataIndex "title"}])


(defn dict-list-page []
  (let [project-dicts @(rf/subscribe [::settings/project-dicts])
        pagination @(rf/subscribe [::settings/project-dicts-pagination])
        pagination (assoc pagination :total (or (:total (first project-dicts)) 0) :onChange (fn [page _] (rf/dispatch [::settings/set-dicts-curr-page page])))]
    ;; 分页有BUG...
    [:div
     (antd/bread-crumb {:separator ">" :items [{:title "系统设置"} {:title "积分项目设置"}] :style {:margin "16px 0"}})
     (page-header)
     [main-content-wrap-container
      [dict-table-header]
      (antd/table {:columns columns :rowKey #(row-key % :id) :dataSource project-dicts :pagination pagination})]]))
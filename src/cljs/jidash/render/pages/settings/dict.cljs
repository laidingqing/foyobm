(ns jidash.render.pages.settings.dict
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [jidash.db.settings :as settings]
            [jidash.render.components.antd :as antd]
            [jidash.render.utils.antd :refer [row-key]]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]))

(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/alert {:type "info" :message "设置项目积分项"})])


(def columns [{:title "编号" :key "id" :dataIndex "id"}
              {:title "项目" :key "description" :dataIndex "description"}
              {:title "编码" :key "code" :dataIndex "code"}
              {:title "名称" :key "title" :dataIndex "title"}])

(defn dict-list-page []
  (let [project-dicts @(rf/subscribe [::settings/project-dicts])]
    [:div
     (antd/bread-crumb {:separator ">" :items [{:title "系统设置"} {:title "积分项目设置"}] :style {:margin "16px 0"}})
     (page-header)
     [main-content-wrap-container
      (antd/table {:columns columns :rowKey #(row-key % :id) :dataSource project-dicts})]]))
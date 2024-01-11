(ns foyobm.render.pages.settings.project
  (:require [foyobm.render.components.antd :as antd]
            [foyobm.render.pages.container.views :refer [main-content-wrap-container]]
            [re-frame.core :as rf]
            [foyobm.db.settings :as settings]
            [foyobm.db.router :as router]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "积分项目设置")
   (antd/alert {:type "info" :message "设置所有可累计积分项目"})])



(defn- render-app-item [item]
  (let [{:keys [title description]} item]
    (antd/list-item
     (antd/list-item-meta {:title title :description description})))
  )

(defn- app-list []
  (let [apps @(rf/subscribe [::settings/applications])]
    [:div
     (antd/text {:strong true} "可设置的积分应用")
     (antd/list {:itemLayout "horizontal" :dataSource apps :renderItem #(render-app-item %)})]))



(defn project-list-page []
  [:div
   (antd/bread-crumb {:separator ">" :items [{:title "系统设置"} {:title "积分项目设置"}] :style {:margin "16px 0"}})
   (page-header)
   [main-content-wrap-container
    [:div {:class "flex flex-row"}
     [:dic {:class "basis-1/4"}
      [app-list]]
     [:div "根据左边选择项配置项目属性"]]]])
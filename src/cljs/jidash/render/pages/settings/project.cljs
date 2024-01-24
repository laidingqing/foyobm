(ns jidash.render.pages.settings.project
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]
            [re-frame.core :as rf]
            [jidash.db.settings :as settings]
            [reagent.core :as r]
            ["@ant-design/icons" :refer [CheckCircleOutlined CloseCircleOutlined]]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "积分项目设置")
   (antd/alert {:type "info" :message "设置所有可累计积分项目"})])



(defn- render-app-item [item]
  (let [{:keys [name]} item
        my-apps @(rf/subscribe [::settings/my-apps])
        activated? (or (some #(= name (:name %)) my-apps) false)]
    (if activated?
      (antd/text "启用")
      (antd/text "未启用"))
    ))


(def columns [{:title "名称" :key "name" :dataIndex "name"}
              {:title "项目名称" :key "title" :dataIndex "title"}
              {:title "描述" :key "description" :dataIndex "description"}
              {:title "获取方式" :key "datalog" :dataIndex "datalog"}
              {:title "是否启用" :key "activated" :dataIndex "activated" :render #(render-app-item %)}])

(defn- render-iterator-props-item [item]
  (let [{:keys [title name value]} item]
    (antd/form-item {:label title :name name :key name}
                    (antd/input {:value value}))))

(defn- render-expand-row [record]
  (let [record (js->clj record :keywordize-keys true)
        {:keys [name title description datalog]} record
        props (-> record :props)
        my-apps (doto @(rf/subscribe [::settings/my-apps]))
        activated? (or (some #(= name (:name %)) my-apps) false)
        form-state (r/atom {:activated activated? :name name :description description :datalog datalog :title title :props []})
        on-check (fn [k] #(swap! form-state assoc k (-> % .-target .-checked)))]
    (antd/form {:layout "horizontal" :labelCol {:span 2}  :onFinish (fn [] ())}
               (antd/form-item {:label "是否开启" }
                (antd/check-box {:defaultChecked activated? :onChange (on-check :activated)}))
               (map #(render-iterator-props-item %) props)
               (antd/form-item {:wrapperCol {:span 14 :offset 2 }}
                (antd/button {:type "primary"} "更新配置")))))


(defn project-list-page []
  (let [apps @(rf/subscribe [::settings/applications])
        pagination @(rf/subscribe [::settings/apps-pagination])]
    [:div
     (antd/bread-crumb {:separator ">" :items [{:title "系统设置"} {:title "积分项目设置"}] :style {:margin "16px 0"}})
     (page-header)
     [main-content-wrap-container
      (antd/table {:columns columns :rowKey "name" :dataSource apps :pagination pagination :expandedRowRender (fn [record] (r/as-element (render-expand-row record)))})]]))

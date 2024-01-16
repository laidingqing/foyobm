(ns foyobm.render.pages.settings.project
  (:require [foyobm.render.components.antd :as antd]
            [foyobm.render.pages.container.views :refer [main-content-wrap-container]]
            [re-frame.core :as rf]
            [foyobm.db.settings :as settings]
            [reagent.core :as r]
            ["@ant-design/icons" :refer [CheckCircleOutlined CloseCircleOutlined]]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "积分项目设置")
   (antd/alert {:type "info" :message "设置所有可累计积分项目"})])



(defn- render-app-item [item]
  (let [{:keys [name title description datalog]} item
        my-apps @(rf/subscribe [::settings/my-apps])
        activated? (or (some #(= name (:name %)) my-apps) false)
        avatar (if activated? (r/as-element [:> CheckCircleOutlined]) (r/as-element [:> CloseCircleOutlined]) )
        ]
    (antd/list-item {:actions [(antd/link {:href "#" :onClick #(rf/dispatch [::settings/select-application {:name name :datalog datalog}])} "设置")]}
                    (antd/list-item-meta {:title title :description description :avatar avatar}))))


(defn- app-list []
  (let [apps @(rf/subscribe [::settings/applications])]
    [:div
     (antd/list {:bordered true :header (antd/text {:strong true} "可设置的积分应用") :itemLayout "horizontal" :dataSource apps :renderItem #(render-app-item %)})]))


(defn- app-form []
  (let [form-state (r/atom {:name ""
                            :datalog ""
                            :activated false})
        on-check (fn [k] #(swap! form-state assoc k (-> % .-target .-checked)))]
    (let [initial-values @(rf/subscribe [::settings/application])
          my-apps @(rf/subscribe [::settings/my-apps])
          activated? (or (some #(= name (:name %)) my-apps) false)]
      (reset! form-state (merge initial-values {:activated activated?}))
      (antd/form {:layout "vertical" :onFinish (fn []
                                                 (rf/dispatch [::settings/create-project @form-state]))}
                 (antd/form-item {:label "应用名称" :name "name"}
                                 [antd/input {:value (:name @form-state)
                                              :disabled true}])
                 (antd/form-item {:label "获取数据方式" :name "datalog"}
                                 (antd/select {:style {:width "200px"} :defaultValue (:datalog @form-state) :options [{:value "webhook" :label "webhook"}
                                                                                                                      {:value "timer" :label "定时获取"}
                                                                                                                      {:value "const" :label "固定积分"}]}))
                 (antd/form-item {:name "activated"}
                                 (antd/check-box {:defaultChecked activated? :onChange (on-check :activated)} "是否启用"))
                 (antd/form-item
                  [antd/button {:htmlType "submit" :size "large" :type "primary" :style {:width "100%"}} "更新"])))))

(defn project-list-page []
  [:div
   (antd/bread-crumb {:separator ">" :items [{:title "系统设置"} {:title "积分项目设置"}] :style {:margin "16px 0"}})
   (page-header)
   [main-content-wrap-container
    [:div {:class "flex flex-row"}
     [:div {:class "basis-1/4"}
      [app-list]]
     [:div {:class "basis-2/4" :style {:padding-left "20px"}}
      [app-form]]]]])
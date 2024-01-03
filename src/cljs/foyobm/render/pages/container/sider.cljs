(ns foyobm.render.pages.container.sider
  (:require [foyobm.render.components.antd :as antd]
            ["@ant-design/icons" :refer [HomeOutlined DashboardOutlined AppstoreOutlined SettingOutlined UserOutlined ReadOutlined SafetyOutlined]]
            [reagent.core :as r]))



(defn menu-items []
  [{:key "1" :label "首页" :icon (r/as-element [:> HomeOutlined]) :title "首页"}
   {:key "2" :icon (r/as-element [:> AppstoreOutlined]) :label "企业管理"  :title "企业管理" :children [{:key "2.1" :label "编辑企业信息"}
                                                     {:key "2.2" :label "用户信息管理"}
                                                     {:key "2.3" :label "分组管理"}
                                                     {:key "2.4" :label "权限管理"}]}
   {:key "3" :icon (r/as-element [:> DashboardOutlined]) :label "积分制" :children [
                                                                                {:key "3.1" :label "我的积分"}
                                                                                {:key "3.2" :label "申报积分"}
                                                                                 {:key "3.3" :label "排行榜"}]}
   {:key "4" :icon (r/as-element [:> SettingOutlined]) :label "系统设置" :children [{:key "4.1" :label "积分项目设置"}
                                                                                {:key "4.2" :label "积分规则设置"}]} ])


(defn- side-menu []
  (fn []
    (let [items (menu-items)]
      [antd/menu {:mode "vertical" :inlineCollapsed false :items items}])))


(defn- side-header []
  (fn []
    [:div {:style {:margin "16px"}} (antd/text "#FoyoBM")]))



(defn- side-user-info []
  (fn []
    [antd/menu {:mode "inline" :items [{:key "1" :label "支持" :icon (r/as-element [:> ReadOutlined])}
                                       {:key "2" :label "个人信息" :icon (r/as-element [:> SafetyOutlined])}]}]
    )
  
  )

(defn- side-footer []
  (fn []
    [:div {:style {:position "absolute" :bottom 0 :margin-bottom "10px"}}
     [side-user-info]
     (antd/divider)
     [:div {:style {:display "flex" :row-gap "8px" :column-gap "8px" :align-items "center" :padding "0 5px"}}
      [:div (antd/avatar {:icon (r/as-element [:> UserOutlined]) :style {:backgroundColor "#87d068"}})]
      [:div {:style {:flex-grow 1 :flex-basis "0%" :min-width "0px"}}
       [:p "赖文清"]
       [:span "laidingqing@me.com"]]]]))

(defn page-sider []
  [antd/layout-sider
   [:div {:style {:display "flex" :flex-direction "column"}}
    [side-header]
    (antd/divider)
    [:div {:style {:flex-grow 1 :overflow-y "auto"}} [side-menu]]
    (antd/divider)
    [side-footer]]
   ])
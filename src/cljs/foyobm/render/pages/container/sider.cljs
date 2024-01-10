(ns foyobm.render.pages.container.sider
  (:require [foyobm.render.components.antd :as antd]
            [re-frame.core :as rf]
            [foyobm.db.router :as router]
            ["@ant-design/icons" :refer [HomeOutlined DashboardOutlined AppstoreOutlined SettingOutlined UserOutlined ReadOutlined SafetyOutlined]]
            [reagent.core :as r]))



(defn menu-items []
  [{:key "1" :label "首页" :icon (r/as-element [:> HomeOutlined]) :title "首页"}
   {:key "2" :icon (r/as-element [:> AppstoreOutlined]) :label "企业管理"  :title "企业管理" :children [{:key "2.1" :label "我的企业" :onClick #(rf/dispatch [::router/push-state :foyobm.render.routes/company-edit])}
                                                                                                {:key "2.2" :label "用户信息管理" :onClick #(rf/dispatch [::router/push-state :foyobm.render.routes/user-list])}
                                                                                                {:key "2.3" :label "分组管理" :onClick #(rf/dispatch [::router/push-state :foyobm.render.routes/group-list])}
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
      [antd/menu {:style {:borderRight 0} :mode "vertical" :items items}])))


(defn page-sider []
  [antd/layout-sider
   [:div {:style {:display "flex" :flex-direction "column"}}
    [:div {:style {:flex-grow 1 :overflow-y "auto"}} [side-menu]]
    (antd/divider)]
   ])
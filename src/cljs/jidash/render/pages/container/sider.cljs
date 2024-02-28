(ns jidash.render.pages.container.sider
  (:require [jidash.render.components.antd :as antd]
            [re-frame.core :as rf]
            [jidash.db.router :as router]
            [jidash.db.auth :as auth]
            ["@ant-design/icons" :refer [HomeOutlined LogoutOutlined AppstoreOutlined SettingOutlined UserOutlined ReadOutlined SafetyOutlined]]
            [reagent.core :as r]))


(defn menu-items []
  [{:key "1" :label "首页" :icon (r/as-element [:> HomeOutlined]) :title "首页"}
   {:key "2" :label "管理积分" :icon (r/as-element [:> AppstoreOutlined]) :title "管理积分" :onClick #(rf/dispatch [::router/push-state :jidash.render.routes/point-list])}
   {:key "3" :icon (r/as-element [:> SettingOutlined]) :label "系统设置" :children [{:key "3.1" :label "我的企业" :onClick #(rf/dispatch [::router/push-state :jidash.render.routes/company-edit])}
                                                                                {:key "3.2" :label "用户信息管理" :onClick #(rf/dispatch [::router/push-state :jidash.render.routes/user-list])}
                                                                                {:key "3.3" :label "分组管理" :onClick #(rf/dispatch [::router/push-state :jidash.render.routes/group-list])}
                                                                                {:key "3.4" :label "权限管理"}]}
   {:key "1" :label "退出登录" :icon (r/as-element [:> LogoutOutlined]) :title "退出登录" :onClick #(rf/dispatch [::auth/logout])}])


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
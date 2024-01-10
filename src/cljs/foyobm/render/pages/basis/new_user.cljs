(ns foyobm.render.pages.basis.new-user
  (:require [foyobm.render.components.antd :as antd]
            [foyobm.render.pages.container.views :refer [main-content-wrap-container]]
            [foyobm.db.auth :as auth]
            [foyobm.db.router :as router]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "创建新用户")
   (antd/alert {:type "info" :message "为当前组织创建新用户, 创建成功后自动加入本企业。"})])

(defn- new-user-form []
  (let [form-state (r/atom {:email ""
                            :password ""
                            :user_name ""})
        on-change (fn [k] #(swap! form-state assoc k (-> % .-target .-value)))]
    (fn []
      (antd/form {:layout "vertical" :onFinish (fn []
                                                 (rf/dispatch [::auth/login @form-state]))}
                 (antd/form-item {:label "邮箱" :name "email"}
                                 [antd/input {:value (:email @form-state)
                                              :on-change (on-change :email)}])
                 (antd/form-item {:label "全名" :name "user_name"}
                                 [antd/input {:value (:user_name @form-state)
                                              :on-change (on-change :user_name)}])
                 (antd/form-item {:label "初始密码" :name "password"}
                                 [antd/input-password {:value (:password @form-state)
                                                       :on-change (on-change :password)}])
                 (antd/form-item
                  (antd/space
                   [antd/button {:htmlType "submit" :type "primary"} "创建用户"]
                   (antd/link {:onClick #(rf/dispatch [::router/push-state :foyobm.render.routes/user-list])} "取消")))))))



(defn new-user-page []
    [:div
     (antd/bread-crumb {:separator ">" :items [{:title "企业管理"} {:title "用户管理" :href "#" :onClick #(rf/dispatch [::router/push-state :foyobm.render.routes/user-list])} {:title "创建用户"}] :style {:margin "16px 0"}})
     [page-header]
     [main-content-wrap-container
      [new-user-form]]])
(ns jidash.render.pages.auth.register
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [jidash.db.auth :as auth]
            [jidash.db.router :as router]
            [jidash.render.components.antd :as antd]))



(defn login-form []
  ;; not rendered..
  (let [form-state (r/atom {:email ""
                            :password ""})
        on-change (fn [k] #(swap! form-state assoc k (-> % .-target .-value)))]
    (fn []
      (antd/form {:layout "vertical" :onFinish (fn []
                                                 (rf/dispatch [::auth/login @form-state]))}
                 (antd/form-item {:label "邮箱" :name "email"}
                                 [antd/input {:size "large"
                                              :value (:email @form-state)
                                              :on-change (on-change :email)}])
                 (antd/form-item {:label "密码" :name "password"}
                                 [antd/input-password {:size "large"
                                                       :value (:password @form-state)
                                                       :on-change (on-change :password)}])
                 (antd/form-item
                  [antd/button {:htmlType "submit" :size "large" :type "primary" :style {:width "100%"}} "注册"])
                  ;; [antd/button {:htmlType "button" :onClick #(rf/dispatch [::ui/set-dialog :error "errrrrrrr...."])} "dialog"])
                 (antd/form-item
                  [:p "已注册账号? " (antd/link {:href "#" :onClick #(rf/dispatch [::router/push-state :jidash.render.routes/login])} "去登录")])))))


(defn register-page []
  [:div {:class "h-screen bg-no-repeat bg-[length:100%_100%]" :style {:background-image "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')"}}
  [:div {:class "flex items-center justify-center gap-1 flex-col grow"}
   [:div {:style {:display "block"}}
    [:p "注册账号开始使用"]]
   [:div {:class "w-1/5 min-w-96 max-w-md"}
    [login-form]]]])
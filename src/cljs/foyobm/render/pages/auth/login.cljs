(ns foyobm.render.pages.auth.login
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [foyobm.db.auth :as auth]
            [foyobm.db.router :as router]
            [foyobm.render.components.antd :as antd]))




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
                  [antd/button {:htmlType "submit" :size "large" :type "primary" :style {:width "100%"}} "登录"])
                  ;; [antd/button {:htmlType "button" :onClick #(rf/dispatch [::ui/set-dialog :error "errrrrrrr...."])} "dialog"])
                 (antd/form-item
                  [:p "还没有账号? " (antd/link {:href "#" :onClick #(rf/dispatch [::router/push-state :foyobm.render.routes/register])} "去注册一个")]))))
  )


(defn login-page []
  [:div {:class "h-screen bg-no-repeat bg-[length:100%_100%]" :style {:background-image "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')"}}
   [:div {:class "flex items-center justify-center gap-1 flex-col grow"}
    [:div {:style {:display "block"}}
     [:p "助力企业管理，快乐工作"]]
    [:div {:class "w-1/5 min-w-96 max-w-md"} 
     [login-form]
     ]]])
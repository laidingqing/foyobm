(ns jidash.render.pages.auth.login
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [jidash.db.auth :as auth]
            [jidash.db.ui :as ui]
            [jidash.db.router :as router]
            [jidash.render.components.antd :as antd]))




(defn login-form []
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
                  [antd/button {:htmlType "submit" :size "large" :type "primary" :style {:width "100%"}} "登录"]))))
  )


(defn login-page []
  [:div {:class "h-screen bg-no-repeat bg-[length:100%_100%]" :style {:background-image "url('/images/l_bg.png')"}}
   [:div {:class "h-screen flex"}
    [:div {:class "flex items-center justify-center gap-1 flex-col grow"}
     
     [:div {:style {:display "block"}}
      [:p {:class "font-bold"} "研发管理系统: 积分制、OKR"]
      (antd/divider)]
     [:div {:class "w-1/5 min-w-96 max-w-md"}
      [login-form]]]]])
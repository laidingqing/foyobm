(ns foyobm.render.pages.login.page
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [foyobm.render.components.common :refer [form-input button-class]]
            [foyobm.db.auth :as auth]
            [foyobm.db.ui :as ui]))


(defn login-form []
  (let [form-state (r/atom {:email "37505218@qq.com"
                            :password "a123456"})
        on-change (fn [k] #(swap! form-state assoc k (-> % .-target .-value)))]
    (fn []
      [:div {:class "p-10 bg-white rounded-xl drop-shadow-lg sm:w-1/2 xl:w-1/4 w-min-1/2"}
       [:h1 {:class "text-2xl font-semibold mb-4"} "登录至#FoyoBM"]
       [:p {:class "text-sm font-light text-gray-500 dark:text-gray-400 mt-4"}
        "没有账号? "
        [:a {:href "#" :class "font-medium text-primary-600 hover:underline dark:text-primary-500" :on-click #(rf/dispatch [::ui/set-active-page {:page :register}])} "去注册"]]
       [:form {:class "mt-4"
               :on-submit (fn [e]
                            (.preventDefault e)
                            (rf/dispatch [::auth/login @form-state]))}
        [form-input {:id "username"
                     :label "用户名(邮箱)"
                     :value (:email @form-state)
                     :on-change (on-change :email)
                     :required true}]
        [form-input {:id "password"
                     :label "密码"
                     :type "password"
                     :value (:password @form-state)
                     :on-change (on-change :password)}]
        [:button {:class (str button-class " bg-indigo-500 text-white hover:bg-indigo-600")
                  :type "submit"}
         "登录"]]])))


(defn page []
  [:div {:class "w-screen h-screen flex justify-center items-center "}
   [login-form]])
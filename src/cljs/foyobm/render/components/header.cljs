(ns foyobm.render.components.header
  (:require [re-frame.core :as rf]
            ;; ["@heroicons/react/24/outline" :refer [Cog8ToothIcon]]
            [foyobm.db.ui :as ui]
            [foyobm.db.auth :as auth]))


(defn no-auth-right-nav []
  [:div {:class "flex items-center space-x-3"}
   [:a {:href "#" :on-click #(rf/dispatch [::ui/set-active-page {:page :login}]) :class "py-2 px-2 font-medium text-white rounded hover:bg-green-500 hover:text-white transition duration-300"} "登录"]
   [:a {:href "#" :class "py-2 px-2 font-medium text-white rounded hover:bg-green-500 hover:text-white transition duration-300"} "注册"]])

(defn authed-right-nav []
  [:div {:class "flex items-center space-x-3"}
   [:img {:class "h-8 w-8 rounded-full"
          :src "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"}]])


(defn header-view []
  (let [account @(rf/subscribe [::auth/account])]
    [:header
     [:nav {:class "bg-white border-gray-200 dark:bg-gray-800"}
      [:div {:class "mx-auto px-4"}
       [:div {:class "flex justify-between"}
        [:div {:class "flex space-x-7"}
         [:div
          [:a {:class "flex items-center py-4 px-2"}
           [:span {:class "font-semibold dark:text-white text-lg"} "#FoyoBM"]]]
         [:div {:class "hidden md:flex items-center space-x-1"}
          [:a {:href "#" :on-click #(rf/dispatch [::ui/set-active-page {:page :home}]) :class "py-4 px-2 text-white border-b-4 border-green-500 font-semibold"} "首页"]
          [:a {:href "#" :class "py-4 px-2 text-white font-semibold hover:text-green-500 transition duration-300"} "工作台"]
          [:a {:href "#" :on-click #(rf/dispatch [::ui/set-active-page {:page :admin-manage}]) :class "py-4 px-2 text-white font-semibold hover:text-green-500 transition duration-300"} "系统管理"]
          [:a {:href "#" :on-click #(rf/dispatch [::ui/set-active-page {:page :user-manage}]) :class "py-4 px-2 text-white font-semibold hover:text-green-500 transition duration-300"} "企业管理"]]]
        (if account
           (authed-right-nav)
           (no-auth-right-nav))
        ]]]]))
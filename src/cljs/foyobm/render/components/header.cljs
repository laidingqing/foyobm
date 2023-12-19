(ns foyobm.render.components.header
  (:require [re-frame.core :as rf]
            ["@heroicons/react/24/outline" :refer [Cog8ToothIcon]]
            [foyobm.db.ui :as ui]))

(defn no-auth-right-nav []
  [:div {:class "md:flex items-center space-x-3"}
   
   [:a {:href "#" :on-click #(rf/dispatch [::ui/set-active-page {:page :login}]) :class "py-2 px-2 font-medium text-white rounded hover:bg-green-500 hover:text-white transition duration-300"} "登录"]
   [:a {:href "#" :class "py-2 px-2 font-medium text-white rounded hover:bg-green-500 hover:text-white transition duration-300"} "注册"]])

(defn authed-right-nav []
  [:div {:class "md:flex items-center space-x-3"}
   [:> Cog8ToothIcon {:class "text-white w-6 h-6"}]
   [:a {:href "#" :class "py-2 px-2 font-medium text-white transition duration-300"} 
    [:span "#树枝孤鸟"]
    ]]
  )


(defn header-view []
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
        [:a {:href "#" :class "py-4 px-2 text-white font-semibold hover:text-green-500 transition duration-300"} "帮助"]]]
      [no-auth-right-nav]
      ;;[authed-right-nav]
      ]
     ]]])



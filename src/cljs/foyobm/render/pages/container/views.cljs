(ns foyobm.render.pages.container.views
  (:require [foyobm.render.components.antd :as antd]
            [foyobm.render.utils :refer [render-children]]
            [foyobm.render.pages.container.sider :refer [page-sider]]
            [foyobm.render.pages.container.header :refer [page-header]]
            [foyobm.render.components.dialogs :refer [dialog]]))

(defn main-content-wrap-container [header children]
  [:div
   (when header header)
   (into [antd/layout-content {:style {:min-height "100dvh" :padding 24 :background "#FFF" :borderRadius "5px"}}] children)])



(defn authenticated-page-container []
  (fn [& children]
    [dialog]
    (antd/layout
     (antd/layout-header {:class "flex items-center h-[56px] p-0 bg-white" :style {:border-block-end "1px solid rgba(5, 5, 5, 0.06)"}}
      [page-header])
     [antd/layout
      [page-sider]
      [antd/layout {:style {:padding "0 24px 24px"}}
       (into [:<>] [children])]])))

(defn generic-page-container []
  (fn [& children]
    [:div {:class "flex flex-col h-screen"}
     [dialog]
     (render-children children)]))
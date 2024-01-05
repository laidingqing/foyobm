(ns foyobm.render.pages.container.views
  (:require [foyobm.render.components.antd :as antd]
            [foyobm.render.utils :refer [render-children]]
            [foyobm.render.pages.container.sider :refer [page-sider]]
            [foyobm.render.components.dialogs :refer [dialog]]))


(defn authenticated-page-container []
  (fn [& children]
    [dialog]
    [antd/layout
     [page-sider]
     [antd/layout
      (into [antd/layout-content {:style {:min-height "100dvh" :padding "24px" :background "#FFF"}}] children)]]))



(defn generic-page-container []
  (fn [& children]
    [:div {:class "flex flex-col h-screen"}
     [dialog]
     (render-children children)]))
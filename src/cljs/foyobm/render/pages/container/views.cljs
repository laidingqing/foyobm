(ns foyobm.render.pages.container.views
  (:require [foyobm.render.components.antd :as antd]
            [foyobm.render.pages.container.sider :refer [page-sider]]))


(defn authenticated-page-container []
  (fn [& children]
    [antd/layout
     [page-sider]
     [antd/layout
      (into [antd/layout-content {:style {:min-height "100dvh"}}] children)]]))



(defn generic-page-container []
  [:div])
(ns foyobm.render.pages.container.header
  (:require [foyobm.render.components.antd :as antd]))



(defn page-header []
  [:div {:style {:margin-inline "16px"}}
   (antd/title {:level 4 :style {:margin "0"}} "#FoyoBM")])
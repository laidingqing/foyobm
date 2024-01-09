(ns foyobm.render.pages.dashboard.page
  (:require [foyobm.render.components.antd :as antd]))


(defn dash-page []
  (fn []
    [:div {:style {:padding-inline "40px"}}
     (antd/bread-crumb {:separator ">" :items [{:title "首页"} {:title "工作台"}] :style {:marginTop "18px"}})
     [:p "hello, word"]])
  )
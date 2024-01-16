(ns jidash.render.pages.dashboard.page
  (:require [jidash.render.components.antd :as antd]))


(defn dash-page []
  (fn []
    [:div {:style {:padding-inline "40px"}}
     (antd/bread-crumb {:separator ">" :items [{:title "首页"} {:title "工作台"}] :style {:marginTop "18px"}})
     [:p "hello, word"]])
  )
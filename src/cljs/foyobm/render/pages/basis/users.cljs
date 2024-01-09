(ns foyobm.render.pages.basis.users
    (:require [foyobm.render.components.antd :as antd]))
  
  
  
  (defn user-list-page []
     (antd/bread-crumb {:separator ">" :items [{:title "企业管理"} {:title "用户管理"}] :style {:marginTop "18px"}})
     [:p "hello, word"])
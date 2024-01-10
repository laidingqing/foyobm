(ns foyobm.render.pages.basis.company
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [foyobm.db.auth :as auth]
            [foyobm.render.components.antd :as antd]
            [foyobm.render.pages.container.views :refer [main-content-wrap-container]]))

;; form
(defn- edit-form []
  (let [existing-company @(rf/subscribe [::auth/current-company])
        button-str (if existing-company "修改" "创建")
        {:keys [name abbr address telphone]} existing-company
        form-state (r/atom {:name name
                            :abbr abbr
                            :address address
                            :telphone telphone})]
    (fn []
      [:div {:class "flex w-1/2"}
       (antd/form {:layout "vertical" :class "w-full"}
                  (antd/form-item {:label "公司名称" :name "name" :rules [{:required true}]}
                                  [antd/input {:value (:name @form-state)}])
                  (antd/form-item {:label "简称" :name "abbr"}
                                  [antd/input {:value (:abbr @form-state)}])
                  (antd/form-item {:label "地址" :name "address"}
                                  [antd/input {:value (:address @form-state)}])
                  (antd/form-item {:label "联系电话" :name "telphone"}
                                  [antd/input {:value (:telphone @form-state)}])
                  (antd/form-item
                   [antd/button {:htmlType "submit" :size "large" :type "primary" :style {:width "100%"}} button-str]))])))
  

(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5 } "企业信息")
   (antd/alert {:type "info" :message "企业用于维护组织及人员结构!"})])


(defn company-edit-page []
  [:div
   (antd/bread-crumb {:separator ">" :items [{:title "企业管理"} {:title "我的企业"}] :style {:margin "16px 0"}})
   (page-header)
   [main-content-wrap-container 
    [edit-form]]])
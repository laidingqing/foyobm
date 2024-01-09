(ns foyobm.render.pages.basis.company
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [foyobm.db.basis :as basis]
            [foyobm.render.components.antd :as antd]
            [foyobm.render.pages.container.views :refer [main-content-wrap-container]]))

;; form
(defn- edit-form []
  (let [existing-company @(rf/subscribe [::basis/current-company])
        button-str (if existing-company "修改" "创建")
        {:keys [name abbr address telphone]} existing-company
        form-state (r/atom {:name name
                            :abbr abbr
                            :address address
                            :telphone telphone})]
    (fn []
      [:div 
       (antd/form {:layout "vertical"}
                  (antd/form-item {:label "公司名称" :name "name"}
                                  [antd/input {:size "large"
                                               :value (:name @form-state)}])
                  (antd/form-item {:label "简称" :name "abbr"}
                                  [antd/input {:size "large"
                                               :value (:abbr @form-state)}])
                  (antd/form-item {:label "地址" :name "address"}
                                  [antd/input {:size "large"
                                               :value (:address @form-state)}])
                  (antd/form-item {:label "联系电话" :name "telphone"}
                                  [antd/input {:size "large"
                                               :value (:telphone @form-state)}])
                  (antd/form-item
                   [antd/button {:htmlType "submit" :size "large" :type "primary" :style {:width "100%"}} button-str]))])))
  

(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/bread-crumb {:separator ">" :items [{:title "企业管理"} {:title "我的企业信息"}] :style {:margin "16px 0"}})
   (antd/title {:level 5} "企业信息")
   (antd/alert {:type "info" :message "企业用于维护组织及人员结构!"})])


(defn company-edit-page []
  (main-content-wrap-container
   (page-header)
   [[edit-form]]))
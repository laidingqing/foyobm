(ns jidash.render.components.forms
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [jidash.db.settings :as settings]
            [jidash.db.basis :as basis]
            [jidash.db.auth :as auth]
            [jidash.render.components.antd :as antd]))


(defn- convert-to-select-opts [item]
  (let [{:keys [name title]} item]
    {:value name
     :label title}))

(def operator-opts
  [{:value "=" :label "等于"}
   {:value ">" :label "大于"}
   {:value ">=" :label "大于等于"}
   {:value "<" :label "小于"}
   {:value "<=" :label "大于等于"}])

(defn rule-form []
  (let [application @(rf/subscribe [::settings/application])
        applications @(rf/subscribe [::settings/applications])
        name (-> application :name)
        event-rules (first (filter #(= (:name %) name) applications))
        rule-opts (map #(convert-to-select-opts %) (:event-rule-keys event-rules))
        form-state (r/atom {:name ""
                            :field ""
                            :operator ""
                            :target ""
                            :score 0})]
    [:div {:style {:margin "20px 0"}}
     (antd/form {:layout "horizontal" :labelCol {:span 4} :onFinish (fn []
                                                                      ())}
                (antd/form-item {:label "规则名" :name "name"}
                                (antd/select {:style {:width "200px"} :options rule-opts}))
                (antd/form-item {:label "匹配属性" :name "field"}
                                [antd/input {:value (:field @form-state)}])
                (antd/form-item {:label "条件" :name "operator"}
                                (antd/select {:style {:width "200px"} :options operator-opts}))
                (antd/form-item {:label "匹配值" :name "target"}
                                [antd/input {:value (:target @form-state)}])
                (antd/form-item {:label "积分" :name "score"}
                                [antd/input {:value (:score @form-state)}]))]))



(defn new-user-form []
  (let [current-company @(rf/subscribe [::auth/current-company])
        form-state (r/atom {:email ""
                            :password ""
                            :user_name ""
                            :admin false
                            :company_id (:id current-company)
                            :status 0})
        on-change (fn [k] #(swap! form-state assoc k (-> % .-target .-value)))]
    (fn []
      (antd/form {:layout "vertical" :onFinish (fn []
                                                 (rf/dispatch [::basis/create-member-user @form-state]))}
                 (antd/form-item {:label "邮箱" :name "email"}
                                 [antd/input {:value (:email @form-state)
                                              :on-change (on-change :email)}])
                 (antd/form-item {:label "全名" :name "user_name"}
                                 [antd/input {:value (:user_name @form-state)
                                              :on-change (on-change :user_name)}])
                 (antd/form-item {:label "初始密码" :name "password"}
                                 [antd/input-password {:value (:password @form-state)
                                                       :on-change (on-change :password)}])
                 (antd/form-item {:label "" :name "admin"}
                                 (antd/check-box "是否管理员"))
                 
                 (antd/form-item {:label "" :name "status"}
                                 (antd/check-box "是否激活"))
                 (antd/form-item
                  (antd/space
                   [antd/button {:htmlType "submit" :type "primary"} "确定"]))))))

(defn new-project-dict-form []
  (let [current-company @(rf/subscribe [::auth/current-company])
        applications @(rf/subscribe [::settings/applications])
        opts (map #(convert-to-select-opts %) applications)
        form-state (r/atom {:classv ""
                            :code ""
                            :title ""})
        on-change (fn [k] #(swap! form-state assoc k (-> % .-target .-value)))]
   (fn []
     (antd/form {:layout "vertical" :onFinish (fn []
                                                (rf/dispatch [::settings/create-project-dict @form-state]))}
                (antd/form-item {:label "项目" :name "classv"}
                                (antd/select {:style {:width "200px"} :options opts}))
                
                (antd/form-item {:label "编码" :name "code"}
                                [antd/input {:value (:code @form-state)
                                             :on-change (on-change :code)}])
                (antd/form-item {:label "名称" :name "title"}
                                [antd/input {:value (:title @form-state)
                                                      :on-change (on-change :title)}])
                (antd/form-item
                 (antd/space
                  [antd/button {:htmlType "submit" :type "primary"} "确定"]))))))
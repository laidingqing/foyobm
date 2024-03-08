(ns jidash.render.components.forms
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [jidash.db.points :as points]
            [jidash.db.common :as commons]
            [jidash.db.auth :as auth]
            [jidash.db.okr :as okr]
            [jidash.render.components.antd :as antd]))

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
                                                 (rf/dispatch [::commons/create-member-user @form-state]))}
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
                                 (antd/check-box {:on-change (fn [k]
                                                               (let [value (-> k .-target .-checked)]
                                                                 (swap! form-state assoc :admin value)))} "是否管理员"))
                 (antd/form-item {:label "" :name "status"}
                                 (antd/check-box {:on-change (fn [k]
                                                               (let [value (-> k .-target .-checked)
                                                                     admin? (if value 0 1)]
                                                                 (swap! form-state assoc :status admin?)))} "是否激活"))

                 (antd/form-item
                  (antd/space
                   [antd/button {:htmlType "submit" :type "primary"} "确定"]))))))


(defn new-point-form []
  (let [form-state (r/atom {:user_id 0
                            :score 0
                            :title ""
                            :catalog "manual"})
        members @(rf/subscribe [::commons/users]) ;;TODO需要初始化
        members-opts (map (fn [v] {:value (:user_id v) :label (:user_name v)}) members)
        on-change (fn [k] #(swap! form-state assoc k (-> % .-target .-value)))]
   (fn []
     (antd/form {:layout "vertical" :onFinish (fn []
                                                (rf/dispatch [::points/create-user-point @form-state]))}
                (antd/form-item {:label "人员" :name "user_id"}
                                (antd/select {:style {:width "200px"} 
                                              :options members-opts 
                                              :value (str (:user_id @form-state))
                                              :on-change (fn [k] 
                                                           (swap! form-state assoc :user_id (js/parseInt k)))}))
                
                (antd/form-item {:label "积分数" :name "score"}
                                [antd/input-number {:value (str (:score @form-state))
                                                    :on-change (fn [k]
                                                                 (swap! form-state assoc :score (js/parseInt k)))}])
                (antd/form-item {:label "积分标题" :name "title"}
                                [antd/input {:on-change (on-change :title)}])
                (antd/form-item
                 (antd/space
                  [antd/button {:htmlType "submit" :type "primary"} "确定"]))))))



(defn new-okr-form
  "New OKRs"
  [data]
  (let [{:keys [id]} data
        form-state (r/atom {:title ""
                            :user_id 0})]
    (fn []
      (antd/form {:layout "vertical" :onFinish (fn []
                                                 (rf/dispatch [::okr/create-okr @form-state]))}
                 (antd/form-item
                  [antd/input {:defaultValue (:title @form-state) :placeholder "例: 提升产品设计工作及专业知识"}])
                 (antd/form-item
                  (antd/button {:htmlType "submit" :type "primary"} "创建目标"))))))
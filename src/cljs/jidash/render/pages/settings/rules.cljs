(ns jidash.render.pages.settings.rules
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [jidash.db.settings :as settings]
            [jidash.render.components.antd :as antd]
            [jidash.render.utils.antd :refer [row-key]]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/alert {:type "info" :message "设置项目积分规则"})])


(defn- render-list-item [item]
  (let [{:keys [name description datalog]} item]
    (antd/list-item {:actions [(antd/link {:href "#" :onClick #(rf/dispatch [::settings/select-application {:name name :datalog datalog :description description}])} "设置")]}
     (antd/list-item-meta {:title name :description description}))))

(defn- project-list []
  (let [my-apps @(rf/subscribe [::settings/my-apps])]
    (antd/list {:bordered true :dataSource my-apps :renderItem #(render-list-item %)})))



(defn- convert-to-select-opts [item]
  (let [{:keys [name title]} item]
    {:value name
     :label title}))

(defn- priv-rule-form []
  (let [form-state (r/atom {:title "" :description ""})]
    (let [my-apps @(rf/subscribe [::settings/my-apps])
          application @(rf/subscribe [::settings/application])
          applications @(rf/subscribe [::settings/applications])
          name (-> application :name)
          event-rules (first (filter #(= (:name %) name) applications))
          rule-opts (map #(convert-to-select-opts %) (:event-rule-keys event-rules))]
      (antd/form {:layout "vertical" :onFinish (fn []
                                                 ())}
                 (antd/form-item {:label "项目规则" :name "title"}
                                 (antd/select {:style {:width "200px"} :options rule-opts}))))))

(defn- render-rule-item [item]
  (let [{:keys [operator target]} item]
    (antd/text (str operator target))))

(def columns [{:title "规则名称" :key "name" :dataIndex "name"}
              {:title "匹配属性" :key "field" :dataIndex "field"}
              {:title "规则" :key "rule" :render #(render-rule-item %)}
              {:title "积分" :key "score" :dataIndex "score"}])

(def test-data [{:name "迟到" :field "*" :operator "=" :target 2 :score 20}])

(defn- event-rule-list []
  (antd/table {:columns columns :rowKey #(row-key % :id) :dataSource test-data}))

(defn rule-list-page []
  (let [application @(rf/subscribe [::settings/application])]
    [:div
     (antd/bread-crumb {:separator ">" :items [{:title "系统设置"} {:title "积分项目设置"}] :style {:margin "16px 0"}})
     (page-header)
     [main-content-wrap-container
      [:div {:class "flex flex-row"}
       [:div {:class "basis-1/4"}
        [project-list]]
       [:div {:class "basis-2/4" :style {:padding-left "20px"}}
        (antd/button {:type "primary" :style {:marginBottom "20px"} :onClick #(rf/dispatch [::settings/open-rule-form])} "新建规则")
        (antd/divider (str (:name application) " : " (:description application)))
        [event-rule-list]]]]]))
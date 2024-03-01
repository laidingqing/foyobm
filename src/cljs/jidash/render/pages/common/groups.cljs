(ns jidash.render.pages.common.groups
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]
            [jidash.db.auth :as auth]
            [jidash.db.router :as router]
            [jidash.db.common :as common]
            [re-frame.core :as rf]
            [reagent.core :as r]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "管理分组")
   (antd/alert {:type "info" :message "管理系统分组(或一级部门)"})])


(defn- empty-groups []
  (antd/text "当前条件中还没有分组数据"))

(def columns [{:title "编号" :key "id" :dataIndex "id"}
              {:title "名称" :key "name" :dataIndex "name"}])


(defn- group-list []
  (let [groups @(rf/subscribe [::common/groups])
        pagination @(rf/subscribe [::common/groups-pagination])]
    (if (empty? groups)
        [empty-groups]
        (antd/table {:columns columns :rowKey #(:id %) :dataSource groups :pagination pagination}))))

(defn- new-group-form []
  (let [current-company @(rf/subscribe [::auth/current-company])
        form-state (r/atom {:name ""
                            :parent 19
                            :company_id (:id current-company)})
        on-change (fn [k] #(swap! form-state assoc k (-> % .-target .-value)))]
    (fn []
      (antd/form {:layout "vertical" :class "w-1/2" :onFinish (fn []
                                                                (rf/dispatch [::common/create-group @form-state]))}
                 (antd/form-item {:label "名称" :name "name"}
                                 [antd/input {:on-change (on-change :name)}])

                 (antd/form-item
                  (antd/space
                   [antd/button {:htmlType "submit" :type "primary"} "添加组"]))))))

(defn groups-page []
  [:div
   (antd/bread-crumb {:separator ">" :items [{:title "企业管理"} {:title "分组管理"}] :style {:margin "16px 0"}})
   [page-header]
   [main-content-wrap-container
    [new-group-form]
    [group-list]]])
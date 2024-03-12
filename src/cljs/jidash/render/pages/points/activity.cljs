(ns jidash.render.pages.points.activity
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]
            [re-frame.core :as rf]
            [jidash.db.points :as points]
            [jidash.db.router :as router]
            [jidash.render.utils.formatter :refer [activity-catalog-formatter datetime-formatter]]
            [jidash.render.utils.antd :refer [row-key]]
            ["@ant-design/icons" :refer [CheckCircleOutlined CloseCircleOutlined]]
            [reagent.core :as r]))


(defn- page-header [user-name]
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} (str "用户" user-name "的积分变更历史"))])

(def columns [{:title "编号" :key "id" :dataIndex "id"}
              {:title "原积分" :key "pre_score" :dataIndex "pre_score"}
              {:title "变动积分" :key "score" :render (fn [row]
                                                    (let [point (:score row)
                                                          color (if (< point 0) "#f50" "#87d068")]
                                                      (antd/text {:strong true :style {:color color}} point)))}
              {:title "说明" :key "title" :dataIndex "title"}
              {:title "类型" :key "catalog" :render #(activity-catalog-formatter %)}
              {:title "变动时间" :key "created" :dataIndex "created" :render #(datetime-formatter %)}])



(defn- filter-header [] 
  
  [:div

   (antd/flex {:gap "middle" :align "center"}
              (antd/text "时间范围:")
              (antd/range-picker)
              (antd/text "积分类型:")
              (antd/select {:style {:width 100} :options [{:label "手动" :value "manual"}, {:label "JIRA" :value "jira"}]})
              (antd/button "查询")
              )

   (antd/divider)]
  
  )

(defn- render-summary [data]
  (r/as-element
     (antd/table-summary-row
      (antd/table-summary-cell {:index 0} "汇总积分数")
      (antd/table-summary-cell {:index 1 :colSpan 5} "****"))))

(defn activity-list-page []
  (let [user-activities @(rf/subscribe [::points/user-activities])
        {:keys [current pageSize]} @(rf/subscribe [::points/user-activities-pagination])
        pagination {:total (or (:total (first user-activities)) 0)
                    :current current
                    :pageSize pageSize
                    :onChange (fn [k] (rf/dispatch [::points/set-activity-page k]))}
        user-name (:name (first user-activities))]
    [:div
     (antd/bread-crumb {:separator ">" :items [{:title "首页"} {:title "积分管理" :href "#" :onClick #(rf/dispatch [::router/push-state :jidash.render.routes/point-list])} {:title "积分变动"}] :style {:margin "16px 0"}})
     (page-header user-name)
     [main-content-wrap-container
      (filter-header)
      (antd/table {:columns columns 
                   :rowKey #(row-key % :id) 
                   :pagination pagination 
                   :dataSource user-activities
                   :summary #(render-summary %)})]]))

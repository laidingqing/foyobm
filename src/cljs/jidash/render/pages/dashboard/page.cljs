(ns jidash.render.pages.dashboard.page
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.utils.formatter :refer [activity-catalog-formatter datetime-formatter]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [jidash.db.points :as points]
            [jidash.render.utils.antd :refer [row-key]]
            [jidash.db.core :refer [run_mode]]
            ["@ant-design/icons" :refer [SettingOutlined EditOutlined EllipsisOutlined]]))


(def columns [{:title "编号" :key "id" :dataIndex "id"}
              {:title "原积分" :key "pre_score" :dataIndex "pre_score"}
              {:title "变动积分" :key "score" :render (fn [row]
                                                    (let [point (:score row)
                                                          color (if (< point 0) "#f50" "#87d068")]
                                                      (antd/text {:strong true :style {:color color}} point)))}
              {:title "说明" :key "title" :dataIndex "title"}
              {:title "类型" :key "catalog" :render #(activity-catalog-formatter %)}
              {:title "变动时间" :key "created" :dataIndex "created" :render #(datetime-formatter %)}])


(defn dash-page []
   (let [current-point (first @(rf/subscribe [::points/user-points]))
         user-activities @(rf/subscribe [::points/user-activities])
         {:keys [current pageSize]} @(rf/subscribe [::points/user-activities-pagination])
         pagination {:total (or (:total (first user-activities)) 0)
                     :current current
                     :pageSize pageSize
                     :onChange (fn [k] (rf/dispatch [::points/set-activity-page k]))}
         user-name (:user_name (first user-activities))]
     [:div {:style {:padding-inline "40px"}}
      (antd/bread-crumb {:separator ">" :items [{:title "首页"} {:title "工作台"}] :style {:marginTop "18px"}})

      (antd/card {:style {:width 300 :margin "16px 0"}
                  :actions [(r/as-element [:> SettingOutlined])
                            (r/as-element [:> EditOutlined])
                            (r/as-element [:> EllipsisOutlined])]}
                 (antd/card-meta {:title user-name :description (str "当前积分：" (:points current-point))}))

      (antd/table {:columns columns :rowKey #(row-key % :id) :pagination pagination :dataSource user-activities})])
  )
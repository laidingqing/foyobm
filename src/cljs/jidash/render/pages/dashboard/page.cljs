(ns jidash.render.pages.dashboard.page
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.utils.formatter :refer [activity-catalog-formatter datetime-formatter]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [jidash.db.points :as points]
            [jidash.render.utils.antd :refer [row-key]]
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
         point-summary  @(rf/subscribe [::points/point-summary])

         {:keys [current pageSize]} @(rf/subscribe [::points/user-activities-pagination])
         pagination {:total (or (:total (first user-activities)) 0)
                     :current current
                     :pageSize pageSize
                     :onChange (fn [k] (rf/dispatch [::points/set-activity-page k]))}
         {:keys [points]} current-point]
     (js/console.log point-summary)
     [:div {:style {:padding-inline "40px"}}
      (antd/bread-crumb {:separator ">" :items [{:title "首页"} {:title "我的积分"}] :style {:marginTop "18px"}})

      (antd/flex {:gap "middle" :style {:marginTop "10px"}}
                 (antd/card
                  (antd/statistic {:title "总积分"
                                   :value points
                                   :valueStyle {:color "#3f8600"}}))
                 (antd/card
                  (antd/statistic {:title "有效积分"
                                   :value points}))

                 
                 (map (fn [n]
                        (let [{:keys [year month day sum]}  n
                              title (cond
                                      year "年度积分"
                                      month "本月积分"
                                      day "当日积分"
                                      :else "未知时间段的积分")]
                          (antd/card
                           (antd/statistic {:title title
                                            :value sum})))) point-summary) )
      (antd/title {:level 5} "积分事件")

      (antd/table {:size "small" :columns columns :rowKey #(row-key % :id) :pagination pagination :dataSource user-activities})])
  )
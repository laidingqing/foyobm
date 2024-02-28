(ns jidash.render.pages.points.points
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]
            [re-frame.core :as rf]
            [jidash.db.points :as points]
            [jidash.db.router :as router]
            [jidash.render.utils.formatter :refer [datetime-formatter]]
            [jidash.render.utils.antd :refer [row-key]]
            ["@ant-design/icons" :refer [CheckCircleOutlined CloseCircleOutlined]]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "用户积分列表")])

(defn- point-table-header []
  (antd/space {:style {:margin-bottom "16px"}}
              (antd/button {:type "primary" :style {:marginBottom "20px"} :onClick #(rf/dispatch [::points/new-point-form])} "手动分配积分")))

(def columns [{:title "编号" :key "id" :dataIndex "id"}
              {:title "姓名" :key "name" :dataIndex "user_name"}
              {:title "总积分" :key "points" :dataIndex "points"}
              {:title "最后更新" :key "updated" :dataIndex "updated" :render #(datetime-formatter %)}
              {:title "操作" :key "op" :render (fn [row]
                                               (antd/space
                                                (antd/link {:href "#" :onClick #(rf/dispatch [::router/push-state :jidash.render.routes/activity-list {:id (:user_id row)}])} "积分变动")))}])


(defn point-list-page []
  (let [user-points @(rf/subscribe [::points/user-points])
        pagination @(rf/subscribe [::points/user-points-pagination])]
    [:div
     (antd/bread-crumb {:separator ">" :items [{:title "首页"} {:title "管理积分"} ] :style {:margin "16px 0"}})
     (page-header)
     [main-content-wrap-container
      [point-table-header]
      (antd/table {:columns columns :rowKey #(row-key % :id) :dataSource user-points })]]))

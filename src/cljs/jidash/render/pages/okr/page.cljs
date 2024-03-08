(ns jidash.render.pages.okr.page
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]
            [reagent.core :as r]
            [jidash.db.okr :as okr]
            [re-frame.core :as rf]
            ["@ant-design/icons" :refer [EditOutlined]]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "OKRs-目标管理")])

(defn- main-header []
  (antd/space {:style {:margin-bottom "16px"}}
              (antd/button {:type "primary" :style {:marginBottom "20px"} :onClick #(rf/dispatch [::okr/new-okr-form])} "制定目标与关键结果")))


(defn- load-segment-opts-by-d [v]
  (condp = v
    "m" [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
    "q" [1, 2, 3]
    "y" [2024, 2025, 2026]
    )
  )


(defn- filter-header []
  (let [scope-selected (r/atom {:scope "m"})]
    (fn []
      (antd/flex {:align "flex-start" :vertical true :gap "small"}
                 (antd/segmented {:defaultValue "m"
                                  :options [{:label "月度"  :value "m"}, {:label "季度"  :value "q"}, {:label "年度"  :value "y"}]
                                  :onChange (fn [k]
                                              (swap! scope-selected assoc :scope k))})
                 (antd/segmented {:options (load-segment-opts-by-d (:scope @scope-selected))})))))

(def okr-demo-data [{:key 1 :label "提升产品质量" :description "提出优化需求点跟进交付" :items [{:id "1.1" :title "单元测试覆盖率达到60%"}, {:id "1.2" :title "功能测试案例数达到40个"}, {:id "1.3"  :title "每个Sprint进行至少2次回归测试"} ]},
                    {:key 2 :label "丰富部门管理制度" :description "完善激励管理办法，有效促进协作与开发"  :items [{:id "2.1" :title "单元测试覆盖率达到60%"}, {:id "2.2" :title "功能测试案例数达到40个"}, {:id "2.3" :title "每个Sprint进行至少2次回归测试"}]}])


(defn- okr-list [data]
  (antd/flex {:gap "small" :wrap "wrap" :style {:marginTop "16px"}}
   (map (fn [okr]
          (let [{:keys [description label key]} okr]
            [:div {:style {:marginRight "5px"} :key key}
             (antd/badge
              (antd/badge-ribbon {:text "进行中" :color "green" :key key}
                                 (antd/card {:style {:width 350} :actions [(r/as-element (antd/text {:key 1} "级别高")),
                                                                                    (r/as-element (antd/text {:key 2} "剩10天")),
                                                                                    (r/as-element (antd/text {:key 3} "40%"))
                                                                                    (r/as-element (antd/link {:key 4 :onClick #(rf/dispatch [::okr/new-okr-form])} [:> EditOutlined]))]}
                                            (antd/card-meta {:title label :description description}))))]
            )) data)))

(defn okr-page []
  (let [okr-list-items okr-demo-data]
    [:div {:style {:padding-inline "40px"}}
     (antd/bread-crumb {:separator ">" :items [{:title "首页"} {:title "我的积分"}] :style {:marginTop "18px"}})
     (page-header)
     [main-content-wrap-container
      (main-header)
      [filter-header]
      (okr-list okr-list-items)]])
 
)
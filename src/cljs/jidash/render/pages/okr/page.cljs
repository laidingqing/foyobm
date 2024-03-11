(ns jidash.render.pages.okr.page
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]
            [reagent.core :as r]
            [jidash.db.okr :as okr]
            [re-frame.core :as rf]
            ["@ant-design/icons" :refer [MoreOutlined AimOutlined CheckCircleTwoTone ClockCircleOutlined CalendarOutlined EditOutlined]]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "我的OKRs")])

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



(defn render-okr-title [okr]
  (let [{:keys [id title]} okr]
    [:div {:class " flex items-center h-[44px]" :key id}
     [:div {:class "p-4 relative"}
      (antd/text {:strong true :class ""} title)]]))

(defn- key-result-form []
  (antd/flex {:horizontal "true" :gap "middle" :style {:marginTop "10px"}}
             (antd/input {:placeholder "例: 测试案例数提升10%, 至少50个 [[2024/03/20]]"})
             (antd/button "添加关键结果")))


(defn- render-wrap-key-result [item]

  (let [{:keys [title tv unit cv]} item] 
     [:div {:class "w-full rounded-md px-1 mt-1 bg-[#f8fafc] " :style {}}
      [:div {:class "flex items-center relative w-full h-[44px]"}
       (antd/flex {:gap "middle"}
                  (r/as-element [:> CheckCircleTwoTone])
                  (antd/text {:class "text-gray-500"} "KR")
                  (antd/flex {:gap "middle"}
                             (antd/text title)
                             (antd/link {:href "#"} (r/as-element [:> EditOutlined])))
                  [:div {:class "absolute right-0"}
                   (antd/flex {:gap "small"}
                              (antd/badge {:count tv :color "#52c41a"})
                              (antd/badge {:showZero true :count (or cv 0) :color "#faad14"}))])]]))


(defn new-okr-form
  "New OKRs"
  [data]
  (let [{:keys [id]} data
        form-state (r/atom {:title ""})
        on-change (fn [k] #(swap! form-state assoc k (-> % .-target .-value)))]
    (fn []
      (antd/form {:layout "inline" :onFinish (fn []
                                               (rf/dispatch [::okr/create-okr @form-state]))}
                 (antd/flex {:gap "middle" :style {:width "100%" :marginTop "5px"}}
                            [antd/input {:defaultValue (:title @form-state)
                                         :placeholder "创建目标，例: 提升产品设计工作及专业知识"
                                         :on-change (on-change :title)}]
                            (antd/button {:htmlType "submit" :type "primary"} "创建目标"))))))


(defn- render-okr-list-item [objective]
  (let [{:keys [title]} objective]
    (antd/list-item
     (antd/card {:size "small"
                 :title (r/as-element (render-okr-title objective))
                 :extra (r/as-element [:> MoreOutlined])}
                [:div {:class " flex items-center flex-col"}
                 (map #(render-wrap-key-result %) (:key_results objective))
                 [:div {:class "w-full"}
                  (key-result-form)]]))))

(defn okr-page []
  (let [okr-list @(rf/subscribe [::okr/objectives])]
    [:div {:style {:padding-inline "40px"}}
     (antd/bread-crumb {:separator ">" :items [{:title "首页"} {:title "目标管理"}] :style {:marginTop "18px"}})
     (page-header)
     [main-content-wrap-container
      (antd/row
       (antd/col 
                 [filter-header]
                 [new-okr-form]
                 (antd/list {:dataSource okr-list
                             :grid {:gutter 16 :xs 1 :sm 2 :md 2 :lg 3 :xl 3 :xxl 3} 
                             :renderItem #(render-okr-list-item %)
                             :style {:marginTop "10px"}})))]]))
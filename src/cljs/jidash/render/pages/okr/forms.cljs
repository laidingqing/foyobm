(ns jidash.render.pages.okr.forms
  (:require [reagent.core :as r]
            [jidash.db.okr :as okr]
            [re-frame.core :as rf]
            [jidash.db.router :as router]
            [jidash.render.components.antd :as antd]
            ["@ant-design/icons" :refer [MoreOutlined AimOutlined CheckCircleTwoTone ClockCircleOutlined CalendarOutlined EditOutlined]]))



(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "我的OKRs")])


(defn- render-okr-title [okr] 
  [:div {:class "shadow-xl flex items-center bg-[#34d399] h-[44px]"}
   [:div {:class "p-4 relative"}
    (r/as-element [:> AimOutlined]) 
    (antd/tag {:color "gold" :class "ml-2" :icon (r/as-element [:> CalendarOutlined])} "2024/03/31")
    (antd/text {:strong true :class ""} "提高产品质量.")]
   [:div {:class "float-right right-0 absolute text-lg font-bold text-gray-500 font-medium"}
    (r/as-element [:> MoreOutlined])]])


(defn- render-wrap-key-result [okr] 
  [:div {:class "shadow-xl flex items-center flex-col mt-0.5 px-4 py-2 bg-[#a7f3d0]"}
   (antd/badge {:class "w-full"}
    (antd/badge-ribbon {:text "100%" :color "green"}
   [:div {:class "w-full rounded-md px-2 bg-[#ecfeff] " :style {}}
    [:div {:class "flex items-center relative w-full h-[44px]"}
     (antd/flex {:gap "middle"}
                (r/as-element [:> CheckCircleTwoTone])
                (antd/text {:class "text-gray-500 font-medium"} "KR1")
                (antd/flex {:gap "middle"}
                           (antd/text {:strong true} "新增测试案例数不少于40个.")
                           (antd/link {:href "#"} (r/as-element [:> EditOutlined]))))]
    [:div {:class "bg-gray pb-0.5 relative"}
     (antd/flex {:gap "middle"}
                (antd/tag {:color "cyan"} "创建: 2024/03/08")
                (antd/tag {:color "cyan"} "截止: 2024/03/28")
                (antd/tag {:color "cyan"} "当前: 10个"))
     
     ]]))

   (antd/badge {:class "w-full"}
               (antd/badge-ribbon {:text "80%" :color "green"}
   [:div {:class "w-full rounded-md px-2 bg-[#ecfeff] mt-1" :style {}}
    [:div {:class "flex items-center relative w-full h-[44px]"}
     (antd/flex {:gap "middle"}
                (r/as-element [:> ClockCircleOutlined])
                (antd/text {:class "text-gray-500 font-medium"} "KR2")
                (antd/flex {:vertical "vertical"}
                           (antd/text {:strong true} "订单模块回归测试不少于2轮")))]]))
   
   ])

(defn- key-result-form []
  (antd/flex {:horizontal "true" :gap "middle" :style {:marginTop "10px"}}
             (antd/input {:placeholder "例: 测试案例数提升10%, 至少50个"})
             (antd/button "发布关键结果")))

(defn okr-detail-page []
  
  (let [update? true]
    [:div {:style {:padding-inline "40px"}}
     (antd/bread-crumb {:separator ">" :items [{:title "首页"} {:title "目标管理" :href "#" :onClick #(rf/dispatch [::router/push-state :jidash.render.routes/okrs])} {:title "编辑OKRs"}] :style {:marginTop "18px"}})
     (page-header)
     (antd/row
      (antd/col {:xl 16 :md 20}
                (render-okr-title {})
                (render-wrap-key-result {})
                (key-result-form)))])
  
  )
(ns jidash.render.components.dialogs
  (:require [re-frame.core :as rf]
            [jidash.db.ui :as ui]
            [jidash.render.components.antd :as antd]
            [jidash.render.components.forms :refer [new-user-form new-point-form]]))


(def dialog-types
  {:loading {:title "数据加载中"
             :description "..."}
   :info {:title "提示" :description ""}
   :error {:title "错误"
           :description "发生错误了. 请重试或联系服务人员."}
   :auth {:title "认证"
          :description "您访问的应用需要登录权限."}
   :logout {:title "退出?"
            :description "你想要退出应用，确定吗?"}
   :new-user-form {:title "创建/更新用户"
                   :description "创建或更企业用户"}
   :new-point-form {:title "人员积分变动"
                   :description "手动变更用户积分"}})


(defn spin-view []
  (let [{:keys [open?]} @(rf/subscribe [::ui/dialog])]
    (if open?
      (antd/spin {:fullscreen true})
      [:<>])))

(defn dialog-view []
  (let [{:keys [open? type message]} @(rf/subscribe [::ui/dialog])
        title (-> dialog-types type :title)
        description (-> dialog-types type :description)
        message (or message description)]
    (antd/modal {:open open? :title title :footer nil :onCancel #(rf/dispatch [::ui/close-dialog])}
                (antd/divider {:style {:margin "14px 0"}})
                (case type
                  :new-user-form [new-user-form]
                  :new-point-form [new-point-form]
                  [:p {:class "mt-4"} message]))))

(defn dialog []
  (let [{:keys [type]} @(rf/subscribe [::ui/dialog])]
    (case type
      :loading [spin-view]
      [dialog-view])))
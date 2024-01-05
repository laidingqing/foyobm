(ns foyobm.render.components.dialogs
  (:require [re-frame.core :as rf]
            [foyobm.db.ui :as ui]
            [foyobm.render.components.antd :as antd]))


(def dialog-types
  {:loading {:title "数据加载中"
             :description "..."}
   :error {:title "错误"
           :description "发生错误了. 请重试或联系服务人员."}
   :auth {:title "认证"
          :description "您访问的应用需要登录权限."}
   :logout {:title "退出?"
            :description "你想要退出应用，确定吗?"}})


(defn spin-view []
  (let [{:keys [open?]} @(rf/subscribe [::ui/dialog])]
    (if open?
      (antd/spin)
      [:<>])))


(defn error-dialog []
  (let [{:keys [error-message]} @(rf/subscribe [::ui/dialog])]
    (fn [] [:p {:class "mt-4"} error-message])))


(defn dialog-view []
  (let [{:keys [open? type]} @(rf/subscribe [::ui/dialog])]
    (antd/modal {:open open?}
                (case type
                  :error [error-dialog]
                  []))))

(defn dialog []
  (let [{:keys [type]} @(rf/subscribe [::ui/dialog])]
    (case type
      :loading [spin-view]
      [dialog-view])))
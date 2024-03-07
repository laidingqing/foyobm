(ns jidash.render.components.views
  (:require [jidash.db.common :as commons]
            [jidash.render.utils.antd :refer [row-key]]
            [jidash.render.components.antd :as antd]
            [re-frame.core :as rf]))





(def dept-members-columns [{:title "编号" :key "id" :dataIndex "id"}
                           {:title "姓名" :key "name" :dataIndex "user_name"}
                           {:title "操作" :key "op" :render (fn []
                                                            (antd/space
                                                             (antd/link {:href "#"} "移除")))}])



(defn list-members-view []
  
  (let [dept-members @(rf/subscribe [::commons/dept-members])
        all-members @(rf/subscribe [::commons/users])
        members-opts (map (fn [v] {:value (:user_id v) :label (:user_name v)}) all-members)
        {:keys [current pageSize]} @(rf/subscribe [::commons/dept-members-pagination])
        pagination {:total (or (:total (first dept-members)) 0)
                    :current current
                    :pageSize pageSize
                    :onChange (fn [k] (rf/dispatch [::commons/set-dept-member-page k]))}]
    (antd/flex {:vertical true :gap "middle"}
               (antd/flex {:gap "middle"}
                (antd/select {:style {:width "200px"}
                              :options members-opts
                              :on-change (fn [k])})
                (antd/button "添加成员"))
               
               (antd/table {:columns dept-members-columns :rowKey #(row-key % :id) :dataSource dept-members :pagination pagination}))) 

)
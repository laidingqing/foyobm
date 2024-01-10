(ns foyobm.render.pages.basis.users
  (:require [foyobm.render.components.antd :as antd]
            [foyobm.render.pages.container.views :refer [main-content-wrap-container]]
            [re-frame.core :as rf]
            [foyobm.db.basis :as basis]
            [foyobm.db.router :as router]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "用户列表")
   (antd/alert {:type "info" :message "管理组织下的用户信息"})])


(defn- user-table-header [] 
  (antd/space
   (antd/button {:type "primary" :onClick #(rf/dispatch [::router/push-state :foyobm.render.routes/new-user])} "创建用户")))

(defn- user-table-filter []
  [:div])


(defn- empty-members []
  (antd/text "当前条件中还没有成员.")
  )

(def columns [{:title "编号" :key "id" :dataIndex "user_id"}
              {:title "姓名" :key "name" :dataIndex "user_name"}
              {:title "邮箱" :key "email" :dataIndex "email"}])

(defn- users-table []
  (let [members @(rf/subscribe [::basis/members])
        pagination @(rf/subscribe [::basis/members-pagination])]
    (if (empty? members)
      [empty-members]
      (antd/table {:columns columns :rowKey #(:id %) :dataSource members :pagination pagination}))))


(defn user-list-page []
  [:div
   (antd/bread-crumb {:separator ">" :items [{:title "企业管理"} {:title "用户管理"}] :style {:margin "16px 0"}})
   (page-header)
   [main-content-wrap-container
    [user-table-header]
    [user-table-filter]
    [users-table]]])
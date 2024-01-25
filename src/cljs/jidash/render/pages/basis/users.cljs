(ns jidash.render.pages.basis.users
  (:require [jidash.render.components.antd :as antd]
            [jidash.render.pages.container.views :refer [main-content-wrap-container]]
            [re-frame.core :as rf]
            [jidash.db.basis :as basis]
            [jidash.render.utils.antd :refer [row-key]]))


(defn- page-header []
  [:div {:style {:margin "16px 0"}}
   (antd/title {:level 5} "用户列表")
   (antd/alert {:type "info" :message "管理组织下的用户信息"})])


(defn- user-table-header [] 
  (antd/space {:style {:margin-bottom "16px"}}
              (antd/button {:type "primary" :style {:marginBottom "20px"} :onClick #(rf/dispatch [::basis/new-user-form])} "创建用户")))

(defn- user-table-filter []
  [:div])


(defn- empty-members []
  (antd/text "当前条件中还没有成员.")
  )

(defn- render-admin-item [record]
  (if (:admin record)
    (antd/text "是")
    (antd/text "-")
    )
  )

(def columns [{:title "编号" :key "id" :dataIndex "user_id"}
              {:title "姓名" :key "name" :dataIndex "user_name"}
              {:title "邮箱" :key "email" :dataIndex "email"}
              {:title "管理员" :key "admin" :render #(render-admin-item %)}
              {:title "状态" :key "status" :dataIndex "status"}])


(defn- users-table []
  (let [members @(rf/subscribe [::basis/members])
        pagination @(rf/subscribe [::basis/members-pagination])]
    (if (empty? members)
      [empty-members]
      (antd/table {:columns columns :rowKey #(row-key % :id) :dataSource members :pagination pagination}))))


(defn user-list-page []
  [:div
   (antd/bread-crumb {:separator ">" :items [{:title "企业管理"} {:title "用户管理"}] :style {:margin "16px 0"}})
   (page-header)
   [main-content-wrap-container
    [user-table-header]
    [user-table-filter]
    [users-table]]])
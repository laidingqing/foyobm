(ns foyobm.render.pages.user.page
  (:require [re-frame.core :as rf]
            [foyobm.db.ui :as ui]
            [foyobm.render.utils :refer [render-main]]
            [foyobm.render.components.aside :refer [aside-view]]
            [foyobm.render.pages.user.usermanage :refer [user-manage-main]]))


(defn user-main []
  [:div "user manage"])


(defn pages
  [page-name]
  (case page-name
    :main [user-main]
    :user-manage [user-manage-main]))


(defn page []
  (let [active-page @(rf/subscribe [::ui/active-user-page])
        page-component (pages active-page)
        aside [aside-view [{:key "1" :title "企业信息" :childrens [{:key "1.1" :title "编辑企业信息" :href "#"}]}
                           {:key "2" :title "用户管理" :childrens [{:key "2.1" :title "用户" :href "#" :on-click #(rf/dispatch [::ui/set-active-use-page {:page :user-manage}])}
                                                      {:key "2.2" :title "分组" :href "#"}]}
                           {:key "3" :title "权限管理" :childrens [{:key "3.1" :title "用户权限分配" :href "#"}]}]]]
    (render-main [aside] [page-component])))
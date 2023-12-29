(ns foyobm.render.components.aside)


(defn- render-children-item [{:keys [href class on-click title key]}]
  [:li {:key key} [:a {:href href :class class :on-click on-click} title]])

(defn- render-item [{:keys [key title childrens]}]
  [:li {:key key}
   [:a {:class "font-semibold text-slate-900"} title]
   (when childrens
     [:ul {:class "mt-4 space-y-2 border-l border-slate-200 pl-6"}
      (map render-children-item childrens)])]
  )

(defn aside-view [items]
  [:ul {:class "space-y-10 text-sm leading-6 text-slate-700 lg:sticky lg:top-0 lg:-mt-16 lg:h-screen lg:w-72 lg:overflow-y-auto lg:py-16 lg:pr-8 lg:[mask-image:linear-gradient(to_bottom,transparent,white_4rem,white)]"}
   (map render-item items)])
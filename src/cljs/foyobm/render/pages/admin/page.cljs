(ns foyobm.render.pages.admin.page
  (:require [foyobm.render.utils :refer [render-main]]))


(defn admin-aside []
  [:ul {:class "space-y-10 text-sm leading-6 text-slate-700 lg:sticky lg:top-0 lg:-mt-16 lg:h-screen lg:w-72 lg:overflow-y-auto lg:py-16 lg:pr-8 lg:[mask-image:linear-gradient(to_bottom,transparent,white_4rem,white)]"}
   [:li 
    [:a {:class "font-semibold text-slate-900"} "Getting set up"]
    [:ul {:class "mt-4 space-y-2 border-l border-slate-200 pl-6"}
     [:li [:a {:href "#" :class ""} "Requirements"]]
     [:li [:a {:href "#" :class ""} "Optional: Add the Inter font family"]]]]])

(defn admin-main []
  [:div "main"])

(defn page []
  (let [aside (admin-aside)
        main (admin-main)]
    (render-main [aside] [main])))
(ns foyobm.render.utils)


(defn render-children [children]
  (into [:<>] children))

(defn render-main [aside content]
  [:div {:class "relative mx-auto w-full max-w-container flex justify-stretch h-screen pt-5"}
   [:div {:class "mx-auto max-w-[40rem] lg:mx-0 lg:max-w-none lg:flex-none"}
    (into [:<>] aside)
    ]
   [:div {:class "mx-auto mt-20 min-w-0 max-w-[40rem] lg:ml-16 lg:mr-0 lg:mt-0 lg:max-w-[50rem] lg:flex-auto prose-sm prose prose-slate prose-a:font-semibold prose-a:text-sky-500 hover:prose-a:text-sky-600"}
    (into [:<>] content)]]
  )
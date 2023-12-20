(ns foyobm.render.utils)


(defn render-children [children]
  (into [:<>] children))
(ns jidash.render.utils.antd)

(defn row-key [record key]
  (let [record (js->clj record :keywordize-keys true)]
    (key record)))
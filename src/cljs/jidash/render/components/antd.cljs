(ns jidash.render.components.antd
  "Wrapper components for the antd library"
  (:refer-clojure :exclude [list])
  (:require ["antd" :as antd]
            [jidash.render.utils.time :as time]
            [medley.core :as medley]
            [reagent.core :as r]))


(defn- split-attributes
  [args]
  (if (map? (first args))
    [(first args) (rest args)]
    [{} args]))

(defn- transform-render-fn
  [render-fn]
  (fn [& args]
    ;; The render function expects a JS object. Something
    ;; magically turns our nice Clojure
    ;; into JS. Hence we use js->clj
    (r/as-element (apply render-fn (map #(js->clj % :keywordize-keys true) args)))))

(defn- antd-wrapper
  "Wraps an antd component given the raw React component
   and a function to transform its attributes."
  ([component]
   (antd-wrapper component identity))
  ([component attr-fn]
   (fn [& args]
     (let [[attributes children] (split-attributes args)]
       (into [:> component (attr-fn attributes)]
             children)))))




;; Typography

(def title (antd-wrapper antd/Typography.Title))

(def text (antd-wrapper antd/Typography.Text))

(def link (antd-wrapper antd/Typography.Link))

(def paragraph (antd-wrapper antd/Typography.Paragraph))

;; Layout
(def flex (antd-wrapper antd/Flex))
(def row (antd-wrapper antd/Row))
(def col (antd-wrapper antd/Col))

(def layout (antd-wrapper antd/Layout))
(def layout-header (antd-wrapper antd/Layout.Header))
(def layout-content (antd-wrapper antd/Layout.Content))

(def layout-sider (antd-wrapper antd/Layout.Sider
                                #(assoc % :theme "light")))

;; Menu

(def menu (antd-wrapper antd/Menu
                        #(update % :class str " menu")))

(def menu-item (antd-wrapper antd/Menu.Item
                             #(medley/update-existing %
                                                      :icon
                                                      r/create-element)))
(def menu-divider (antd-wrapper antd/Menu.Divider))

(def menu-submenu (antd-wrapper antd/Menu.SubMenu
                                #(medley/update-existing %
                                                         :icon
                                                         r/create-element)))
(def bread-crumb (antd-wrapper antd/Breadcrumb))

;; Form

(def form (antd-wrapper antd/Form))
(def form-item (antd-wrapper antd/Form.Item))

;; Input

(def input (antd-wrapper antd/Input))
(def input-password (antd-wrapper antd/Input.Password))
(def input-number (antd-wrapper antd/InputNumber))

(def text-area (antd-wrapper antd/Input.TextArea))
(def select (antd-wrapper antd/Select))
(def check-box (antd-wrapper antd/Checkbox))

;; option must be called like a function, don't use it as hiccup
(def option (antd-wrapper antd/Select.Option))

(def button (antd-wrapper antd/Button
                          #(medley/update-existing %
                                                   :icon
                                                   r/create-element)))

;; Others

(def list (antd-wrapper antd/List
                        #(-> %
                             (medley/update-existing :renderItem transform-render-fn)
                             (medley/update-existing :header r/as-element)
                             (medley/update-existing :footer r/as-element))))

(def list-item (antd-wrapper antd/List.Item
                             #(medley/update-existing %
                                                      :actions
                                                      (partial map r/as-element))))

(def list-item-meta (antd-wrapper antd/List.Item.Meta
                                  #(medley/update-existing %
                                                           :title
                                                           (partial map r/as-element))))

(def space (antd-wrapper antd/Space
                         #(medley/update-existing %
                                                  :split
                                                  r/as-element)))

(def divider (antd-wrapper antd/Divider))

(defn- update-table-columns
  [columns]
  (map (fn [column]
         (medley/update-existing column
                                 :render
                                 transform-render-fn))
       columns))

(def table (antd-wrapper antd/Table
                         #(update % :columns update-table-columns)))

(def page-header (antd-wrapper antd/PageHeader
                               #(medley/update-existing %
                                                        :extra
                                                        r/as-element)))

(defn dropdown
  [& args]
  (let [[attributes children] (split-attributes args)]
    [:> antd/Dropdown (medley/update-existing attributes
                                              :overlay
                                              r/as-element)
     (into [:span {:style (case (:trigger attributes)
                            "click" {:cursor "pointer"}
                            {:cursor "default"})}]
           children)]))



(def avatar (antd-wrapper antd/Avatar))

(def tooltip (antd-wrapper antd/Tooltip))

(def popconfirm (antd-wrapper antd/Popconfirm))

(def date-picker (antd-wrapper antd/DatePicker
                               #(-> %
                                    (medley/update-existing
                                     :value
                                     time/calendar-date->moment-date)
                                    (medley/update-existing
                                     :onChange
                                     (fn [on-change] (comp on-change
                                                           time/js-date->calendar-date
                                                           (fn [date]
                                                             (.toDate ^js/Date date))))))))

(def spin (antd-wrapper antd/Spin))

(def modal (antd-wrapper antd/Modal
                         #(medley/update-existing % :footer r/as-element)))

(def alert (antd-wrapper antd/Alert))

(def card (antd-wrapper antd/Card))
(def card-meta (antd-wrapper antd/Card.Meta))

(def segmented (antd-wrapper antd/Segmented))
(def collapse (antd-wrapper antd/Collapse))
(def badge (antd-wrapper antd/Badge))
(def badge-ribbon (antd-wrapper antd/Badge.Ribbon))
(def tag (antd-wrapper antd/Tag))
(def dropdown-button (antd-wrapper antd/Dropdown.Button))
(def carousel (antd-wrapper antd/Carousel))
(ns foyobm.render.components.common)

(def button-class
  "w-full transition-all duration-300
   font-medium border border-gray-300 px-4 py-2 text-sm rounded-md
   focus:ring-2 focus:ring-offset-2 focus:ring-blue-500")


(defn button [{:keys [id class type text on-click icon]}]
  [:div {:class "inline-block"}
   [:button {:class (str class "bg-[#509ee3] border border-[#509ee3] items-center shrink h-9 m-0 text-white rounded-md px-4")
             :id id
             :type (or type "button")
             :on-click on-click}
    [:div {:class "ml-0 flex flex-row justify-center items-center"}
     (when icon icon)
     (when text [:h4 {:class "inline ml-2 mb-0 text-white"} text])]]])

(defn form-input [{:keys [id class type label value on-change required? disabled?]}]
  (let [input-class "form-input relative transition-all duration-300 
                     w-full border-gray-300 
                     rounded-lg tracking-wide
                     placeholder-gray-400 bg-white 
                     focus:ring focus:border-indigo-500 focus:ring-indigo-500/20
                     disabled:opacity-40 disabled:cursor-not-allowed"]
    [:div {:class "mb-7"}
     [:label {:class "block font-medium mb-2 text-sm"
              :for id} label]
     [:input {:class (str input-class " " class)
              :type (or type "text")
              :id id
              :value value
              :on-change on-change
              :required required?
              :disabled disabled?}]]))
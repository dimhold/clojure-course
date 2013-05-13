(ns kickstartup.components
  (:require [enfocus.core :as ef])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet dialog-snipp "/html/new_startup.html" [:#modal-dialog]
  [{:keys [width height header body footer]}]
  [:#content] (let [w (if-not (nil? width) (str "width:" width "px;") "")
                    h (if-not (nil? height) (str "height:" height "px;") "")]
                (em/set-attr :style (str w h)))
  [:#header] (em/content header)
  [:#body] (em/content body)
  [:#footer] (em/content footer))

(defn ^:export show-dialog [params]
  (em/at js/document
         [:#dialog-holder] (em/content (dialog-snipp params))))

(defn ^:export hide-dialog []
  (em/at js/document
         [:#dialog-holder] (em/content "")))
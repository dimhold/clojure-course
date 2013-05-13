(ns kickstartup.util
  (:require [clojure.string :as string]
            [cljs.reader :as reader]
            goog.net.XhrIo)
  (:require-macros [enfocus.macros :as em]))

(defn -escape [text]
  (-> text
      (string/replace "&" "%26")
      (string/replace "+" "%2B")
      (string/replace ":" "%3A")))

(defn -make-post-data [data]
  (.replace
   (reduce str
           (map #(str % "=" (-escape (js/encodeURI (% data)))) (keys data)))
   (js/RegExp. ":" "g") "&"))

(defn ^:export get-data [url func]
  (.send goog.net.XhrIo
         url
         #(func (reader/read-string (.getResponseText (.-target %))))
         "GET"))

(defn ^:export post-data
  ([url func] (post-data url func {}))
  ([url func data]
     (.send goog.net.XhrIo
            url
            #(func (reader/read-string (.getResponseText (.-target %))))
            "POST"
            (-make-post-data data))))


(defn ^:export get-element-value [id]
  (get (em/from js/document
                :value [id] (em/get-prop :value))
       :value))

(defn ^:export set-element-attr [id attr val]
  (em/at js/document
         [id] (em/set-attr attr val)))

(defn ^:export remove-element-attr [id attr]
  (em/at js/document
         [id] (em/remove-attr attr)))


(defn ^:export get-element-attr [id attr]
  (em/from
   (em/select [id]) (em/get-attr attr)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Some handy functions

(defn show-element [id]
  (em/at js/document
         [id] (em/set-attr :style "display:block;")))

(defn hide-element [id]
  (em/at js/document
         [id] (em/set-attr :style "display:none;")))
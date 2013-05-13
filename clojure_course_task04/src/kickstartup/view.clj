(ns kickstartup.view
  (:require [me.raynes.laser :as l]
            [clojure.java.io :refer [file]]))


(def main-html
  (l/parse
   (slurp (clojure.java.io/resource "public/html/index.html"))))

(def all-html
  (l/parse
   (slurp (clojure.java.io/resource "public/html/all.html"))))

(def new-startup-html
  (l/parse
   (slurp (clojure.java.io/resource "public/html/new_startup.html"))))




(def panel (l/select all-html (l/class= "bigpanel")))

 (l/defragment panel-with-startup panel [data]
  (l/element= :h3)(l/content (get data :title))
  (l/class= "pnt-value")(l/content (str (get data :points)))
  (l/class= "video") (l/content (l/unescaped (get data :video)))
  (l/element= :p) (l/content (get data :content))
  (l/element= :a) (l/attr :href (get data :link))
  (l/element= :form) (l/attr :action (str "/kickstartup/plus/" (get data :id)))
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pages


;TODO: Refactor. copy-paste :(
(defn show-3-top-startups [startups]
  (l/document main-html
    (l/id= "hero1-header") (l/content (get (first startups) :title))
    (l/id= "hero1-video") (l/content (l/unescaped (get (first startups) :video)))
    (l/id= "hero1-content") (l/content (get (first startups) :content))
    (l/id= "hero1-link") (l/attr :href (get (first startups) :link))
    
    (l/id= "hero2-header") (l/content (get (second startups) :title))
    (l/id= "hero2-video") (l/content (l/unescaped (get (second startups) :video)))
    (l/id= "hero2-content") (l/content (get (second startups) :content))
    (l/id= "hero2-link") (l/attr :href (get (second startups) :link))
    
    (l/id= "hero3-header") (l/content (get (last startups) :title))
    (l/id= "hero3-video") (l/content (l/unescaped (get (last startups) :video)))
    (l/id= "hero3-content") (l/content (get (last startups) :content))
    (l/id= "hero3-link") (l/attr :href (get (last startups) :link))))


(defn show-startups [startups]
  (l/document all-html
    (l/id= "main") (l/content
    (for [st startups]
      (panel-with-startup st)))))

(defn show-add-startup-form []
  (l/document new-startup-html))

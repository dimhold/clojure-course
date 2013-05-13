(ns kickstartup.views.main
  (:use compojure.core)
  (:require [me.raynes.laser :as l]
            [compojure.route :as route]
            [kickstartup.models.model :as db]
            [clojure.java.io :refer [file]]))

(def main-html
  (l/parse
   (slurp (clojure.java.io/resource "public/html/index.html"))))

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

(defn show-top []
  (show-3-top-startups (db/get-top3-startups)))

(defroutes app-routes
  (GET "/kickstartup" [] (show-top)))

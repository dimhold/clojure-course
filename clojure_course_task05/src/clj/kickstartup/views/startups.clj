(ns kickstartup.views.startups
  (:use compojure.core)
  (:require [me.raynes.laser :as l]
            [compojure.route :as route]
            [kickstartup.models.model :as db]
            [clojure.java.io :refer [file]]))

(def all-html
  (l/parse
   (slurp (clojure.java.io/resource "public/html/all.html"))))

(def panel (l/select all-html (l/class= "bigpanel")))

(l/defragment panel-with-startup panel [data]
  (l/element= :h3) (l/content (get data :title))
  (l/class= "pnt-value") (l/content (str (get data :points)))
  (l/class= "pnt-value") (l/attr :id (str "points" (get data :id)))
  (l/class= "video") (l/content (l/unescaped (get data :video)))
  (l/element= :p) (l/content (get data :content))
  (l/element= :a) (l/attr :href (get data :link))
  (l/element= :button) (l/attr :onclick (str "kickstartup.main.plus(" (get data :id) ");" )))

(defn show-startups [startups]
  (l/document all-html
    (l/id= "main") (l/content
    (for [st startups]
      (panel-with-startup st)))))

(defroutes app-routes
  (GET "/kickstartup/all" [] (show-startups (db/get-all-startups))))

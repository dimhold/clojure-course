(ns kickstartup.views.new
  (:use compojure.core)
  (:require [me.raynes.laser :as l]
            [compojure.route :as route]
            [kickstartup.models.model :as db]
            [clojure.java.io :refer [file]]))

(def new-startup-html
  (l/parse
   (slurp (clojure.java.io/resource "public/html/new_startup.html"))))

(defn show-new-startup-dialog []
   (l/document new-startup-html))

(defn create-new-startup [item]
  (db/create-startup (merge item {:points 1}))
  "ok"
  )

(defroutes app-routes
  (GET "/kickstartup/new" [] (show-new-startup-dialog))
  (POST "/kickstartup/new" req (create-new-startup (:params req))))

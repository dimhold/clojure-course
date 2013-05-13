(ns kickstartup.views.plus
  (:use compojure.core)
  (:require [me.raynes.laser :as l]
            [kickstartup.models.points :as db]
            [clojure.java.io :refer [file]]))

(defn plus-one-startup [id]
  (do
    (db/update-points id)
    (str "\"" id ":" (db/get-points id) "\"")))

(defroutes app-routes
  (POST "/kickstartup/plus/:id" [id] (plus-one-startup id)))

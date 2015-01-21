; Please, take a look at @piyushmandovra version of this script in pull-request: https://github.com/dimhold/clojure-course/pull/1
(ns kickstartup.models.points
  (:require [kickstartup.db :as db])
  (:use [korma db core]))

(defentity startup)

;TODO: Refactor: ugly function
(defn get-points [id]
  (get 
    (first 
      (select startup
      (fields :points)
      (where {:id id}))
  ) :points))

;Two requests to db. Yes, very ugly... Investigate counter in clojure
(defn update-points [id]
  (update startup 
    (set-fields {:points (inc (get-points id))})
    (where {:id id})))

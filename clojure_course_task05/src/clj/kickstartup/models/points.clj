; Please, take a look at @piyushmandovra version of this script in pull-request: https://github.com/dimhold/clojure-course/pull/1
(ns kickstartup.models.points
  (:require [kickstartup.db :as db]
            [korma.core :as kc]))

(kc/defentity startup)

;TODO: Refactor: ugly function
(defn get-points [id]
  (get-in (kc/select startup (kc/fields :points) (kc/where {:id id})) [0 :points]))

;Two requests to db. Yes, very ugly... Investigate counter in clojure
(defn update-points [id]
  (kc/update startup  (kc/set-fields {:points (kc/raw "points+1")}) (kc/where {:id id})))

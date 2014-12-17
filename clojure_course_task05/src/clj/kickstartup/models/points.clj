(ns kickstartup.models.points
  (:require [kickstartup.db :as db]
            [korma.core :as kc]))

(kc/defentity startup)

;TODO: Refacot: ugly function
(defn get-points [id]
  (get-in (kc/select startup (kc/fields :points) (kc/where {:id id})) [0 :points]))

;Two requests to db. Yes, very ugly... Investigate counter in clojure
(defn update-points [id]
  (kc/update startup  (kc/set-fields {:points (raw "points+1")}) (kc/where {:id id})))

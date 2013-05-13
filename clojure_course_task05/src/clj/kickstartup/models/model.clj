(ns kickstartup.models.model
  (:require [kickstartup.db :as db])
  (:use [korma db core]))

(defentity startup)

(defn get-all-startups []
  (select startup
    (order :id :desc)))

(defn get-top3-startups []
  (select startup
   (order :points :desc)
   (limit 3)))

(defn create-startup [item]
  (insert startup (values item)))

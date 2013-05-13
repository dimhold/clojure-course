(ns kickstartup.main
  (:require [kickstartup.util :as util]
            [enfocus.core :as ef]
            [kickstartup.components :as comps]
            [clojure.browser.repl :as repl])
  (:require-macros [enfocus.macros :as em])
  (:use [jayq.core :only ($ append attr on hide)]))

;; (repl/connect "http://localhost:9000/repl")

(declare redraw-page)



(defn reaload [] 
	(.reload (.-location js/window)))



(defn update-points [data]
  (let [id (first (.split data ":"))
        points (last (.split data ":"))]
    (em/at js/document 
      [(str "#points" id)] (em/content points))))

(defn ^:export plus [id]
 (util/post-data (str "/kickstartup/plus/" id) update-points))

(defn ^:export show-new-startup-dialog []
	(comps/show-dialog))

(defn ^:export hide-new-startup-dialog []
	(comps/hide-dialog))

(defn get-value [id]
	(.-value (.getElementById js/document id)))

(defn ^:export add-startup [id]
	(util/post-data 
		(str "/kickstartup/new?content=" (get-value "inputContent")
			 "&link=" (get-value "inputLink")
			 "&title=" (get-value "inputTitle")
			 "&video=" (get-value "inputVideo")) reaload))

(ns kickstartup.handler
  (:use compojure.core
        [korma db core])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [noir.util.middleware :as noir]
            [noir.session :as session]
            [kickstartup.db :as db]
            [noir.response :as resp]
            [kickstartup.models.model :as model]
            [kickstartup.views.startups :as startups-view]
            [kickstartup.views.plus :as plus-view]
            [kickstartup.views.new :as new-startup-view]
            [kickstartup.views.main :as main-view]))

(defdb korma-db db/default-conn)

(defroutes app-routes
  
  (GET "/" [] (resp/redirect "/kickstartup"))
  
  (route/resources "/") 
  (route/not-found "Not Found"))

(def app
  (-> [startups-view/app-routes
       main-view/app-routes
       new-startup-view/app-routes
       plus-view/app-routes
       (handler/site app-routes)]
      noir/app-handler
      noir/war-handler))

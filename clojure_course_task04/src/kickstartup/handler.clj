(ns kickstartup.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [kickstartup.model :as model]
            [kickstartup.view :as view]
            [ring.util.response :as resp]
            [noir.util.middleware :as noir]
            [noir.session :as session]
            ))


(defn pint [s-int]
  (Integer/parseInt s-int))


(defn show-top []
  (view/show-3-top-startups (model/get-top3-startups)))

(defn show-startups []
  (view/show-startups (model/select-startups)))

 (defn add-startup []
   (view/show-add-startup-form))

(defn create-new-startup [item] 
  (do
    (model/create-startup (merge item {:points 1}))
    (show-startups)))

(defn plus-one-startup [id]
  (do
    (model/update-points id)
    (show-startups)
  ))


(defroutes app-routes
  
  (GET "/" [] (resp/redirect "/kickstartup"))
  

  (GET "/kickstartup" [] (show-top))

  (GET "/kickstartup/all" [] (show-startups))

  (GET "/kickstartup/new" [] (add-startup))

  (POST "/kickstartup/new" req (create-new-startup (:params req)))

  (POST "/kickstartup/plus/:id" [id] (plus-one-startup id))

  (route/resources "/") 
  (route/not-found "Not Found"))

(def app
  (->
   [(handler/site app-routes)]
   noir/app-handler
   noir/war-handler
   ))

(comment
  ;; Function for inspecting java objects
  (use 'clojure.pprint)
  (defn show-methods [obj]
    (-> obj .getClass .getMethods vec pprint))
)

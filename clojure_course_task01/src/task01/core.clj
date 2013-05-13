(ns task01.core
  (:require [pl.danieljanus.tagsoup :refer :all])
  (:gen-class))

;; @alexott: defn определяет "top level" объект, так что лучше ее вынести за пределы функции 
;; @dimhold: fixed

;; @alexott: если же имеется необходимость определить функцию внутри другой функции, то можно воспользоваться (let [func (fn [...))
;; или letfn.
(defn get-hrefs [data accum]
;; @alexott: при использовании reduce, для читабельности удобней пользоваться (fn [arg1 arg2] - тогда легче будет понимать код
;; @dimhold: fixed
  (reduce
    (fn [arg1 arg2]
        (if (coll? arg2)
           (if-not (nil? (some #{{:class "r"}} arg2))
              [(get arg2 2)]
              (reduce conj arg1 (get-hrefs arg2 arg1)))))
accum data))

(defn get-links []
  (let [data (vec (parse "clojure_google.html"))]
    (map #(get-in % [1 :href]) (get-hrefs data []))))

(defn -main []
  (println (str "Found " (count (get-links)) " links!")))

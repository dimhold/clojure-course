(ns hire-doer.tests
  (:require [hire-doer.main :as m]))

(def success 0)

(defn my-test-success-example []
  (assert (m/testing-function 2)))

(defn my-test-error-example []
  (assert (m/testing-function 3)))

(defn ^:export run []
  (.log js/console "Example test started.")
  (my-test-success-example)
  (my-test-error-example)
  -1)
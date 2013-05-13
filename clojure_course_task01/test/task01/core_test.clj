(ns task01.core-test
  (:use clojure.test
        task01.core))

(deftest count-test
  (testing "Links count"
    (is (= (count (get-links)) 10))))

(deftest count-test
  (testing "Links count"
    (is (= (count (get-links)) 10))))

(deftest check-links
  (testing "Check all 10 links"
    (let [data (apply hash-set (get-links))]
      (is (contains? data "http://clojure.org/"))
      (is (contains? data "http://en.wikipedia.org/wiki/Clojure"))
      (is (contains? data "http://joyofclojure.com/"))
      (is (contains? data "https://github.com/clojure/clojure"))
      (is (contains? data "https://github.com/functional-koans/clojure-koans"))
      (is (contains? data "http://www.reddit.com/r/Clojure/"))
      (is (contains? data "http://stackoverflow.com/questions/tagged/clojure"))
      (is (contains? data "http://en.wikibooks.org/wiki/Clojure_Programming"))
      (is (contains? data "http://clojure.com/")))))

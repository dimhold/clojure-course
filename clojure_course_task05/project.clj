(defproject kickstartup "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [me.raynes/laser "1.1.1"]
                 [lib-noir "0.4.9"]
                 [enfocus "1.0.1"]
                 [jayq "2.3.0"]
                 [mysql/mysql-connector-java "5.1.24"]
                 [korma "0.3.0-RC4"]
                 [com.cemerick/piggieback "0.0.4"]]
  :plugins [[lein-ring "0.8.3"]
            [lein-cljsbuild "0.3.0"]]
  :ring {:handler kickstartup.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}}

  :aot []
  :source-paths ["src/clj" "src/cljs"]
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  
  :cljsbuild
  {:builds
   [
    {:source-paths ["src/cljs"],
     :id "main",
     :compiler
     {:pretty-print true,
      :output-to "resources/public/js/main.js",
      :warnings true,
      :externs ["externs/jquery-1.9.js"],
      ;; :optimizations :advanced,
      :optimizations :whitespace,
      :print-input-delimiter false}}
    


    {:source-paths ["src/cljs" "test-cljs"],
     :id "test",
     :compiler
     {:pretty-print true,
      :output-dir ".clojurescript-output-test",
      :output-to "resources/public/js/test.js",
      :optimizations :whitespace}}],
   :test-commands
   {"unit"
    ["phantomjs"
     "phantomjs/unit-test.js"
     "http://localhost:3000/html/test.html"]}}
  :war {:name "kickstartup.war"})



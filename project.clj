(defproject nona "0.1.0-SNAPSHOT"
  :description "A static-html generator written in clojure"
  :url "http://github.com/obmarg/nona"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj"]
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1934"]
                 [markdown-clj "0.9.11"]
                 [enlive "1.0.0"]
                 [clj-yaml "0.4.0"]
                 [fs "1.3.2"]
                 [clj-logging-config "1.9.10"]]

  :profiles {:dev {:dependencies [[midje "1.4.0"]]}}

  :plugins [[lein-cljsbuild "0.3.4"]]

  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]

              :compiler {:output-to "resources/public/js/nona.js"}

              :optimizations :whitespace

              :pretty-print true}]}

  :main nona.core)

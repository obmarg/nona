(defproject nona "0.1.0-SNAPSHOT"
  :description "A static-html generator written in clojure"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.4.0"]
                 [markdown-clj "0.9.11"]
                 [enlive "1.0.0"]
                 [clj-yaml "0.4.0"]
                 [fs "1.3.2"]]
  :main nona.core)

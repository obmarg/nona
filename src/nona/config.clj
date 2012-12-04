(ns nona.config
  (:require [clojure.java.io :as io])
  )

(def defaults
  {:templates-dir "templates"
   :output-dir "output"}
  )

(defonce config (atom defaults))

(defn get-config
  "Gets the configuration for nona"
  []
  (swap! config merge (read-string (slurp "conf.clj")))
  )

(defn set-config
  "Sets configuration values.  Mostly for REPL use"
  [new-config]
  (swap! config merge new-config)
  )

(defn get-template-file
  [layout-name]
  (io/file (:templates-dir @config) (str layout-name ".html"))
  )

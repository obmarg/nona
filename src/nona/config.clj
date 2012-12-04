(ns nona.config
  (:require [clojure.java.io :as io])
  )

(def defaults
  {:templates-dir "templates"
   :output-dir "output"
   :default-layout "page"}
  )

(defonce config (atom defaults))

(defn load-config
  "Loads the configuration for nona from a specified file"
  [filename]
  (swap! config merge (read-string (slurp filename)))
  )

(defn get-config
  "Gets the value of a config setting"
  [setting]
  (@config setting)
  ; TODO: Maybe want to except if setting isn't set...
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

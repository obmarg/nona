(ns nona.files
  (:require [clojure.java.io :as io]
            [clj-yaml.core :as yaml])
  )

(defn- split-data
  "Takes the data from a file map, and splits it into metadata & data"
  [{:keys [data] :as file-data}]
  (if-let [[_ header body] (re-find #"(?ms)---(.*?)---(.*)" data)]
    (assoc file-data
      :metadata (yaml/parse-string header) 
      :data body
      )
    file-data))

(defn- load-source-file
  [file]
  (if-not (.isDirectory file)
    {:name (.getName file) :path (.getPath file) :data (slurp file)}
    ))

(def load-source-files
  (comp
    set
    (partial map split-data)
    (partial remove nil?)
    (partial map load-source-file)
    file-seq
    io/file)
    )
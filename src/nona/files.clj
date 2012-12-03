
(ns nona.files
  (:require [clojure.java.io :as io])
  )

(defn- load-source-file
  [file]
  (if-not (.isDirectory file)
    {:name (.getName file) :path (.getPath file) :data (slurp file)}
    ))

(def load-source-files
  (comp
    set
    (partial remove nil?)
    (partial map load-source-file)
    file-seq
    io/file)
    )

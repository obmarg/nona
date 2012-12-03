(ns nona.core
  (:require [clojure.java.io :as io])
  )

(defn- source-file
  [file]
  (if-not (.isDirectory file)
    {:name (.getName file) :path (.getPath file) :data "something"}
    ; TODO: Make data a future that gets the files data
    ))

(defn source-files
  "Creates a list of "
  [dir] (set (remove nil? (map source-file (file-seq (io/file dir)))))
  ; TODO: Decide if a set is the best format.  Map of filenames -> data
  ;       could work better
  )

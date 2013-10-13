(ns nona.files
  (:require [clojure.java.io :as io]
            [fs.core :as fs])
  (:use 	[nona.config :only (get-config)])
  )

(declare 
 split-data load-source-file relative-path 
 replace-extension add-dest-path get-template-file)

(defn get-source-files
  "Returns a sequence of source files"
  [path]
  (fs/with-cwd 
    (fs/file (get-config :base-dir) path)
    (->> "."
         fs/file
         file-seq
         (remove fs/directory?)
         )))

(defn load-files
  "Takes a sequence of files, returns a sequence of
   [file contents] pairs"
  [files]
  (for [file files] [file (slurp file)])
  )

(def load-source-files (comp load-files get-source-files))

(defn save-dest-file
  [name content]
  (fs/with-cwd 
   (apply fs/file (map get-config [:base-dir :dest-dir]))
    (let [file (fs/file name)]
      (io/make-parents file)
      (spit file content)
      )))

(defn get-template-file
  [filename]
  (fs/file 
   (get-config :base-dir) 
   (get-config :templates-dir) 
   filename)
  )

(defn relative-path
  "Calculates a relative path"
  ([path] (relative-path fs/*cwd* path))
  ([base path]
  (let [base (.toURI (io/as-file base))
        path (.toURI (io/as-file path))]
    (.getPath (.relativize base path))
    )))

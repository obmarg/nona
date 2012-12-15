(ns nona.files
  (:require [clojure.java.io :as io]
            [clj-yaml.core :as yaml]
            [fs.core :as fs])
  (:use 	[nona.config :only (get-config)])
  )

(declare 
 split-data load-source-file relative-path 
 replace-extension add-dest-path get-template-file)

(defn load-source-files
  [path]
  (fs/with-cwd 
    (fs/file (get-config :base-dir) path)
    (->> "."
         fs/file
         file-seq
         (remove fs/directory?)
         (map load-source-file)
         set
         )))

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

(defn- load-source-file
  [file]
  (let [[name extension] (fs/split-ext file)
        rel-path (relative-path file)
        dest-path (io/file (fs/parent rel-path) (str name ".html"))
        file-data (slurp file)
        [metadata data] (split-data file-data)]
    {:name name 
     :ext extension
     :src-path (relative-path file)
     :dest-path (relative-path dest-path)
     :metadata metadata
     :data data
     }
   ))

(defn- split-data
  "Takes the contents of a file and splits it into metadata & data"
  [data]
  (if-let [[_ header body] (re-find #"(?ms)---(.*?)---(.*)" data)]
    [(yaml/parse-string header) body]
    [nil data]))

(defn- relative-path
  "Calculates a relative path"
  ([path] (relative-path fs/*cwd* path))
  ([base path]
  (let [base (.toURI (io/as-file base))
        path (.toURI (io/as-file path))]
    (.getPath (.relativize base path))
    )))

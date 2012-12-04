(ns nona.files
  (:require [clojure.java.io :as io]
            [clj-yaml.core :as yaml])
  )

(defn- split-data
  "Takes the data from a page map, and splits it into metadata & data"
  [{:keys [data] :as page}]
  (if-let [[_ header body] (re-find #"(?ms)---(.*?)---(.*)" data)]
    (assoc page
      :metadata (yaml/parse-string header) 
      :data body
      )
    page))

(defn- load-source-file
  [file]
  (if-not (.isDirectory file)
    {:name (.getName file) 
     :src-path (.getPath file)
     :data (slurp file)}
    ))

(defn- relative-path
  "Calculates a relative path"
  [base path]
  (let [base (.toURI (io/as-file base))
        path (.toURI (io/as-file path))]
    (.getPath (.relativize base path))
    ))

(defn- replace-extension
  "Replaces the extension on a file"
  [ext file]
  (let [ext-pos (.lastIndexOf file ".")
        basename (subs file 0 ext-pos)
        result (str basename "." ext)]
    result
    ))

(defn- add-dest-path
  [base-path page]
  (let [src-path (:src-path page)
        rel-path (relative-path base-path src-path)
        dest-path (replace-extension "html" rel-path)
        ]
  	(assoc page :dest-path dest-path)
  	))

(defn load-source-files
  [path]
  (->> path
       io/file
       file-seq
       (map load-source-file)
       (remove nil?)
       (map (partial add-dest-path path))
       (map split-data)
       set
       ))

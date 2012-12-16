(ns nona.posts
  (:require [clj-yaml.core :as yaml]
            [fs.core :as fs]
            [clojure.string :as string]
            )
  (:use [clojure.string :only (split)]
        [markdown.core :only (md-to-html-string)]
        [nona.config :only (get-config get-layout)]
        [nona.files :only (relative-path)]
        ))

(declare transform-data split-metadata filename-metadata)

(defn make-post
  "Makes a post from some input data and file details"
  [file contents]
  (let [[name extension] (fs/split-ext file)
        rel-path (relative-path file)
        [metadata data] (split-metadata contents)]
    {:name name 
     :content (transform-data data)
     :src-path rel-path
     :metadata (merge (filename-metadata name) metadata)
     }))

(defn get-render-context
  "Takes a post and creates a map of all data relevant to rendering the post.
   Suitable for adding to a page before rendering"
  [post]
  (merge (:metadata post) {:content (:content post)})
  )

;
; Metadata extraction functions
;
(defn- split-metadata
  "Takes the contents of a file and splits it into metadata & data"
  [data]
  (if-let [[_ header body] (re-find #"(?ms)---\s+(.*?)---\s+(.*)" data)]
    [(yaml/parse-string header) body]
    [nil data]))

(defn- filename-metadata
  "Takes a filename (without extension) and extracts a date and time from it"
  [filename]
  (if-let [[_ date title] (re-matches #"^(\d{4}-\d{2}-\d{2})?-?(.*)" filename)]
    (let [rv {:title (string/replace title \- \space)}]
      (if date (assoc rv :date date) rv)
      ) 
    {}))

; 
; Rendering functions
;

(defmulti ^:private transform-data
  "Does the transform for a page"
  (fn [ext data] ext))

(defmethod transform-data ".md"
  [ext data]
  (md-to-html-string data))

(defmethod transform-data ".html"
  [ext data]
  data)


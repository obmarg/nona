(ns nona.render
  (:require [net.cgrand.enlive-html :as html]
            [nona.files :as files])
  (:use [clojure.string :only (split)]
        [markdown.core :only (md-to-html-string)]
        [nona.config :only (get-config get-layout)])
  )

(declare 
  transform-data
  get-template 
  )

(defn render-page
  "Takes a page, returns a rendered string"
  [templates page]
  (let [layout (get-in page [:metadata :layout] (get-config :default-layout))
        template (templates layout)]
    (->> page
         transform-data
         (assoc page :content)
         template
         (apply str)
         )))

;
; Templating functions
;

(declare
  create-template 
  create-snippet
  attr-match
  handle-data-text
  get-page-data
  )

(defn create-templates
  "Function that creates templates from layouts
   Returns a map of layout-name -> template"
  [layouts]
  (zipmap (keys layouts) (map create-template (vals layouts)))
  )

(defn- create-template
  "Creates a template function for a single page from a layout"
  [{:keys [template snippet insertpoint]}]
  (let [template (-> template
                     files/get-template-file
                     html/html-resource)
        snippet (create-snippet template snippet)]
    (html/template 
      template
      [page]
      [:#title] (html/content (:name page))
      [insertpoint] (html/content (snippet page))
      )))

(defn- create-snippet
  "Creates a snippet for use in templates"
  [template selector]
  (html/snippet template [selector]
    [item]
    [(html/attr? :data-text)] (handle-data-text (partial get-page-data item))
    ))

(defn- handle-data-text
  "Returns a function that handles elements with data-text attribs
   page-data should be a function that accepts a seq of keywords"
  [page-data]
  (fn [node]
    (let [data-text (first (html/attr-values node :data-text))
          keywords (vec (map keyword (split data-text #"\.")))]
      (assoc node :content (page-data keywords))
      )))

(defn- get-page-data
  "Function that gets data from a page using some keywords"
  [page keywords]
  (cond 
    (= (take 2 keywords) [:page :content]) (html/html-snippet (:content page))
    (= (first keywords) :page) (get-in 
                                 page 
                                 (cons :metadata (subvec keywords 1))
                                 ""
                                 )
    :else ""
    ))

(defn- attr-match
  "Returns an enlive selector for elements with an attribute
   matching a regexp"
  [regexp] 
  (html/pred (fn
    [element]
    (let [attrs (map str (keys (:attrs element)))
          matches (map #(re-matches regexp %) attrs)]
      (true? (some matches))
      ))))

; 
; Rendering functions
;

(defmulti ^:private transform-data
  "Does the transform for a page"
  (fn [page] (:ext page)))

(defmethod transform-data ".md"
  [page]
  (md-to-html-string (:data page)))

(defmethod transform-data ".html"
  [page]
  (:data page))

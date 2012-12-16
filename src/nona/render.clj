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
  attr-matches
  attr-match?
  handle-data-text
  handle-data-each
  get-page-data
  split-keywords
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

(def ^:private data-each-regexp #"^data-each-.*")

(defn- create-snippet
  "Creates a snippet for use in templates"
  [template selector]
  (html/snippet template [selector]
    [item]
    [(html/attr? :data-text)] 
      (handle-data-text (partial get-page-data item))
    [(attr-match? data-each-regexp)] 
      (handle-data-each (partial get-page-data item))
    ))

(defn- handle-data-text
  "Returns a function that handles elements with data-text attribs
   page-data should be a function that accepts a vec of keywords"
  [page-data]
  (fn [node]
    (let [data-text (get-in node [:attrs :data-text])
          keywords (split-keywords data-text)]
      (print "in-text keywords: " keywords "\n")
      ; TODO: Maybe want to make it so this does nothing
      ; if page-data returns nil (and make it so it does
      ; if applicable).  This way we can avoid awkward
      ; ordering problems where the results of one
      ; transform are overwritten by another.
      (assoc node :content (page-data keywords))
      )))

(defn- handle-data-each
  "Returns a function that handles elements with data-each-* attribs.
   page-data should be a function that accepts a vector of keywords"
  [page-data]
  (fn [node]
    ; Note: For now, this assumes there's only a single
    ; data-each on each element (and only nested one level deep)
    ; enlive transforms deep first, so I don't think nested
    ; each- would really work out right anyway...
    (let [kw (first (attr-matches data-each-regexp node))
          bind-name (subs (name kw) 9)
          items (->> [:attrs kw] 
                     (get-in node) split-keywords page-data)]
      (print "in-each\n")
      ((html/clone-for [item items]
          [(html/attr? :data-text)]
            (handle-data-text (constantly item))
          ) 
         node
         ))))

(defn- split-keywords
  "Takes a string of . seperated words, returns a vector of keywords"
  [s] 
  (vec (map keyword (split s #"\.")))
  )

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

(defn- attr-matches
  "Gets a seq of attributes that match a regular expression"
  [regexp element]
  {:pre [(instance? java.util.regex.Pattern regexp)]}
  (->> element :attrs keys 
       (map name) 
       (map #(re-matches regexp %))
       (remove nil?)
       (map keyword)
       ))

(defn- attr-match?
  "Returns an enlive selector for elements with an attribute
   matching a regexp"
  [regexp] 
  (html/pred (fn
    [element]
    (seq (attr-matches regexp element))
    )))

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

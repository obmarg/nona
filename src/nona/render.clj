(ns nona.render
  (:require [net.cgrand.enlive-html :as html]
            [nona.files :as files])
  (:use [markdown.core :only (md-to-html-string)]
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
    ; TODO: Replace this stuff with actual selectors.
    [:.content] (html/html-content (:content item))
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

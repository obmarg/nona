(ns nona.render
  (:require [net.cgrand.enlive-html :as html]
            [nona.files :as files])
  (:use [markdown.core :only (md-to-html-string)]
        [nona.config :only (get-config)])
  )

(declare 
 create-template 
 get-template 
 transform-data)

(def ^:private templates (atom {}))

(defn render-page
  "Takes a page, returns a rendered string"
  [page]
  (let [layout (get-in page [:metadata :layout] (get-config :default-layout))]
    (assoc page :content (transform-data page))
    (apply str ((get-template layout) page))
    ))

(defn- create-template
  "Creates a template function from a filename"
  [filename]
  (html/template 
   filename
   [page]
   [:#title] (html/content (:name page))
   [:#content] (html/html-content (:content page))
   ))

(defn- get-template
  "Gets a named template, loading if required"
  [name]
  (let [template (@templates name)]
    (if template
      template
      (let
        [template (create-template 
                   (files/get-template-file name))]
        (swap! templates assoc name template)
        template
        ))
    ))

(defmulti ^:private transform-data
  "Does the transform for a page"
  (fn [page] (:ext page)))

(defmethod transform-data ".md"
  [page]
  (md-to-html-string (:data page)))

(defmethod transform-data ".html"
  [page]
  (:data page))
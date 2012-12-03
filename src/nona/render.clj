(ns nona.render
  (:require [net.cgrand.enlive-html :as html])
  (:use [markdown.core :only (md-to-html-string)])
  )

(defn create-template
  "Creates a template function from a filename"
  [filename]
  (html/template 
   filename
   [info]
   [:#title] (html/content (:name info))
   [:#content] (html/html-content (:content info))
   ))

(defn render-page
  "Takes a template and a page, returns a rendered string"
  [template {:keys [data] :as info}]
  (->>
   data
   md-to-html-string
   (assoc info :content)
   template
   (apply str)
   ))
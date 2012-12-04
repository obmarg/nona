(ns nona.render
  (:require [net.cgrand.enlive-html :as html]
            [nona.config :as config])
  (:use [markdown.core :only (md-to-html-string)])
  )

(defn- create-template
  "Creates a template function from a filename"
  [filename]
  (html/template 
   filename
   [info]
   [:#title] (html/content (:name info))
   [:#content] (html/html-content (:content info))
   ))

(def ^:private templates (atom {}))

(defn- get-template
  "Gets a named template, loading if required"
  [name]
  (let [template (@templates name)]
    (if template
      template
      (let
        [template (create-template 
                   (config/get-template-file name))]
        (swap! templates assoc name template)
        template
        ))
    ))

(defn render-page
  "Takes a page, returns a rendered string"
  [{:keys [data metadata] :as page}]
  (let [template (get-template (:layout metadata))]
    (->>
     data
     md-to-html-string
     (assoc page :content)
     template
     (apply str)
     )))
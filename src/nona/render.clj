(ns nona.render
  (:require [net.cgrand.enlive-html :as html]
            [nona.files :as files])
  (:use [markdown.core :only (md-to-html-string)]
        [nona.config :only (get-config)])
  )

(declare create-template get-template)

(def ^:private templates (atom {}))

(defn render-page
  "Takes a page, returns a rendered string"
  [{:keys [data metadata] :as page}]
  (let [layout (:layout metadata (get-config :default-layout))
        template (get-template layout)]
    (->>
     data
     md-to-html-string
     (assoc page :content)
     template
     (apply str)
     )))

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
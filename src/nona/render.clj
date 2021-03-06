(ns nona.render
  (:require [net.cgrand.enlive-html :as html]
            [nona.files :as files]
            [clojure.tools.logging :as log]
            )
  (:use [clojure.string :only (split)]
        [nona.config :only (get-config get-layout)])
  )

(declare
  transform-data
  get-template
  )

(defn make-template-context
  "Takes some variables, and makes a context suitable for passing in to a
   template"
  [config indexes page]
  ; TODO: could probably put some preconditions on this function
  {:config config
   :indexes indexes
   :page page
   })

(defn render
  "Renders a context using a template, returns a string"
  [template context]
  (->> context template (apply str))
  )

;
; Templating functions
;

(declare
  create-template
  create-snippet
  attr-matches
  attr-match?
  handle-data-text
  handle-data-content
  handle-data-each
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
      [context]
      [:#title] (html/content (get-in context [:page :title]))
      [insertpoint] (html/content
                      (for
                        [post (get-in context [:page :posts])]
                        (snippet {:post post})
                        ))
      )))

(def ^:private data-each-regexp #"^data-each-.*")

(defn- create-snippet
  "Creates a snippet for use in templates"
  [template selector]
  (html/snippet template [selector]
    [context]
    [(html/attr? :data-text)]
      (handle-data-text (partial get-in context))
    [(html/attr? :data-content)]
      (handle-data-content (partial get-in context))
    [(attr-match? data-each-regexp)]
      (handle-data-each (partial get-in context))
    ))

(defn- replace-content-handler
  "Returns a function that handles elements needing replaced.
   Should be used for data-text & similar handlers
   Params:
      attr      The html attribute to get the keywords from
      getter    A function that gets content to replace
                when passed in a sequence of keywords"
  [attr getter]
  (fn [node]
    (let [data-text (get-in node [:attrs attr])
          keywords (split-keywords data-text)]
      (log/trace "in replace-handler [" attr ", " getter "]")
      (log/trace "keywords: " keywords)
      (log/trace "replacement: " (getter keywords))
      ; TODO: Maybe want to make it so this does nothing
      ; if page-data returns nil (and make it so it does
      ; if applicable).  This way we can avoid awkward
      ; ordering problems where the results of one
      ; transform are overwritten by another.
      (assoc node :content (getter keywords))
      )))

(def ^:private handle-data-text
  (partial replace-content-handler :data-text)
  )

(defn- handle-data-content
  [getter]
  (replace-content-handler :data-content #(apply html/html-snippet (getter %1)))
  )

(defn- handle-data-each
  "Returns a function that handles elements with data-each-* attribs.
   context should be a function that accepts a vector of keywords"
  [context]
  (fn [node]
    ; Note: For now, this assumes there's only a single
    ; data-each on each element (and only nested one level deep)
    ; enlive transforms deep first, so I don't think nested
    ; each- would really work out right anyway...
    (let [kw (first (attr-matches data-each-regexp node))
          bind-name (subs (name kw) 9)
          items (->> [:attrs kw]
                     (get-in node) split-keywords context)]
      (log/trace "processing handle-data-each")
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


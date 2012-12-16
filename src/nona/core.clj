(ns nona.core
  (:use (nona
          [config :only (load-config set-config get-config)]
          [render :only (render-page create-templates make-template-context)]
          [files :only (load-source-files save-dest-file)]
          [utils]
          ))
  (:require [clojure.string :as string]
            )
  (:gen-class))

(declare page-from-post)

(defn -main
  "I don't do a whole lot ... yet."
  [basefolder & args]
  (set-config :base-dir basefolder)
  (load-config (str basefolder "/config.clj"))
  (let [templates (create-templates (get-config :layouts))]
    (doseq
      [page (load-source-files "pages")]
      (print "Outputting" (:name page) "to" (:dest-path page) "\n")
      (save-dest-file (:dest-path page) (render-page templates page))
      ))
  (println "Done!"))

(def default-indexes
  ; TODO: Need to add templates to this also
  ;       Probably want something to generate output filenames too
  ;       Though might be able to use grouping etc. to do that 
  ;       automatically
  {:index       {:title ""
                 :path ""
                 :page-grouping nil
                 :order :date
                 :limit 10
                 :max-pages 1}
   :archives    {:title "Blog Archive"
                 :path "/blog/archive/"
                 :page-grouping nil
                 :order :date
                 :limit nil
                 :max-pages nil}
   :categories  {:title "Category: %s"
                 :path "/blog/categories/"
                 :page-grouping :categories
                 :order :date
                 :limit nil
                 :max-pages nil}
   })

(declare index-page)

(defn gen-index-pages
  ; TODO: Could maybe rename this generate-index and have it return data/call
  ; another function.  Might be useful if we need index data elsewhere (which
  ; I think we will)
  "This function takes an index definition and some posts and generates index
   pages"
  [index posts]
  ; First, we group, sort & split data according to the input parameters.
  (let [groups (group-by-single-key
                 #(get-in % [:metadata (:page-grouping index)]) posts)]
      (for [[group posts] groups]
        (index-page index group (sort-by (:order index) posts))
        )))

(defn- index-page
  "Function that takes a list of posts and generates an index page
   using them."
  [index group posts]
  ; TODO: this next one should use file somehow...
  {:dest-path (str (:path index) (or group "") "index.html") 
   ; TODO: may need to add in sitewide title here
   :title (string/replace (:title index) "%s" (str group)) 
   :posts posts
   })


(defn page-from-post
  "This function takes a post and generates it's page"
  [post]
  {:dest-path ; TODO: generate a proper path to post (from date & title)
   :title ; Post title - Site title
   :posts [(make-template-context post)]
   ; TODO: Might need template info in here too...
   }
  )

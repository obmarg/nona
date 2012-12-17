(ns nona.core
  (:use
    clj-logging-config.log4j
    (nona
      [config :only (load-config set-config get-config)]
      [render :only (render create-templates make-template-context)]
      [files :only (load-source-files save-dest-file)]
      [posts :only (get-dest-path get-render-context make-post) :as posts]
      [utils]
      ))
  (:require [clojure.string :as string]
            [clojure.java.io]
            [clojure.tools.logging :as log]
            )
  (:gen-class))

(def logging-started (atom false))

(defn stdout-logging
  "Function that starts logging to stdout.  Useful for using the repl"
  ([level]
   (set-logger! :level level :out (fn [ev] (println (:message ev))))
   (reset! logging-started true))
  ([] (stdout-logging :debug)))

(declare page-from-post gen-index-pages default-indexes)

(defn get-posts
  "Loads source files and makes posts from them"
  []
  (map #(apply make-post %1) (load-source-files "pages"))
  )

(defn get-templates
  "Loads layouts from config & creates templates"
  []
  (create-templates (get-config :layouts))
  )

(defn gen-indexes
  "Generates all the default indexes using post data"
  [posts]
  (mapcat #(gen-index-pages (second %1) posts) default-indexes)
  )

(defn -main
  [basefolder & args]
  (if (not @logging-started) (do
                               (set-logger!)
                               (reset! logging-started true)))
  (set-config :base-dir basefolder)
  (load-config (str basefolder "/config.clj"))
  (let [templates (get-templates)
        posts (get-posts)
        pages (concat (map page-from-post posts) (gen-indexes posts))
        make-context (partial make-template-context {} {})
        get-template #(templates (:layout %1))]
    (log/debug "posts " posts "\n")
    (doseq
      [page pages]
      (log/debug "Outputting " (:title page)
             " to " (:dest-path page)
             " using layout " (:layout page)
             "\n")
      (save-dest-file (:dest-path page) (->> page
                                             make-context
                                             (render (get-template page))
                                             ))))
  (log/info "Done!"))

(def default-indexes
  {:index       {:title ""
                 :path ""
                 :page-grouping nil
                 :order :date
                 :limit 10
                 :max-pages 1
                 :layout "page"}
   :archives    {:title "Blog Archive"
                 :path "/blog/archive/"
                 :page-grouping nil
                 :order :date
                 :limit nil
                 :max-pages nil
                 :layout "page"}
   :categories  {:title "Category: %s"
                 :path "/blog/categories/"
                 :page-grouping :categories
                 :order :date
                 :limit nil
                 :max-pages nil
                 :layout "page"}
   })

(declare index-page)

(defn gen-index-pages
  ; TODO: Could maybe rename this generate-index and have it return data/call
  ; another function.  Might be useful if we need index data elsewhere (which
  ; I think we will)
  ; Also think that the arguments might be better the other way around...
  "This function takes an index definition and some posts and generates index
   pages"
  [index posts]
  ; First, we group, sort & split data according to the input parameters.
  (let [group-key (:page-grouping index)
        groups (group-by-single-key
                 #(get-in % [:metadata group-key]) posts)]
        ;TODO: Maybe want to split each group at this point based on limit
        ;      and mas-pages (partition is probably the function to use...) 
    (for [[group posts] groups :when (or (nil? group-key) group)]
      (index-page index group (sort-by (:order index) posts))
      )))

(defn- get-index-path
  "Generates an index path (and strips leading slash)"
  [index group]
  (subs
    (str (clojure.java.io/file (:path index) (or group "") "index.html"))
    1
    ))

(defn- index-page
  "Function that takes a list of posts and generates an index page
   using them."
  [index group posts]
  {:dest-path (get-index-path index group)
   ; TODO: may need to add in sitewide title here
   :title (string/replace (:title index) "%s" (str group))
   :posts (map posts/get-render-context posts)
   :layout (:layout index)
   })


(defn page-from-post
  "This function takes a post and generates it's page"
  [post]
  {:dest-path (posts/get-dest-path post)
   :title "Temporary" ; TODO: should be Post title - Site title
   :posts [(posts/get-render-context post)]
   :layout (get-in post [:metadata :layout] (get-config :default-layout))
   })

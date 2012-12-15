(ns nona.core
  (:use (nona
         [config :only (load-config set-config get-config)]
         [render :only (render-page create-templates)]
         [files :only (load-source-files save-dest-file)])
        )
  (:gen-class))

(declare process-page)

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

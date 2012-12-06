(ns nona.core
  (:use (nona
         [config :only (load-config set-config)]
         [render :only (render-page)]
         [files :only (load-source-files save-dest-file)])
        )
  (:gen-class))

(declare process-page)

(defn -main
  "I don't do a whole lot ... yet."
  [basefolder & args]
  (set-config :base-dir basefolder)
  (load-config (str basefolder "/config.clj"))
  (doseq
    [page (load-source-files "pages")]
    (print "Outputting" (:name page) "to" (:dest-path page) "\n")
    (save-dest-file (:dest-path page) (render-page page))
    )
  (println "Done!"))

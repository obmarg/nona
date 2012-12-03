(ns nona.config)

(def defaults
  {:templates-dir "templates"}
  )

(defn get-config
  "Gets the configuration for nona"
  []
  (merge defaults (read-string (slurp "conf.clj")))
  )

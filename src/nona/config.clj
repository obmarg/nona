(ns nona.config)

(def ^:private defaults
  {:templates-dir "templates"
   :output-dir "output"
   :default-layout "page"
   :dest-dir "out"}
  )

(defonce ^:private config (atom defaults))

(defn load-config
  "Loads the configuration for nona from a specified file"
  [filename]
  (swap! config merge (read-string (slurp filename)))
  )

(defn get-config
  "Gets the value of a config setting"
  [setting]
  (@config setting)
  ; TODO: Maybe want to except if setting isn't set...
  )

(defn set-config
  "Sets configuration values.  Mostly for REPL use"
  ([new-config-map] (swap! config merge new-config-map))
  ([key value] (swap! config assoc key value))
  )

(defn get-layout
  "Gets details of a layout from the config"
  [name]
  ((get-config :layouts) name)
  )
